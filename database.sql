-- ============================================================================
-- Blinkit-Style Grocery Delivery App - MySQL 8.0 Database Schema
-- ============================================================================
-- Database: blinkit_db
-- Author: E1 Agent
-- Created: 2025-08-15
-- Description: Complete normalized database schema for grocery delivery application
-- ============================================================================

-- Drop existing database if exists and create fresh
DROP DATABASE IF EXISTS blinkit_db;
CREATE DATABASE blinkit_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blinkit_db;

-- ============================================================================
-- TABLE: users
-- Description: Stores user account information and authentication details
-- ============================================================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL COMMENT 'Bcrypt hashed password',
    phone VARCHAR(15),
    profile_image VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL DEFAULT NULL,
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB COMMENT='User account and authentication data';

-- ============================================================================
-- TABLE: categories
-- Description: Product categories (e.g., Vegetables, Fruits, Dairy, Snacks)
-- ============================================================================
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    display_order INT DEFAULT 0 COMMENT 'Order in which categories are displayed',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_display_order (display_order),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB COMMENT='Product categories';

-- ============================================================================
-- TABLE: products
-- Description: Product catalog with pricing and inventory details
-- ============================================================================
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    brand VARCHAR(100),
    image_url VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL COMMENT 'Current selling price',
    original_price DECIMAL(10, 2) DEFAULT NULL COMMENT 'Original price before discount',
    discount_percentage INT DEFAULT 0,
    unit VARCHAR(50) DEFAULT 'piece' COMMENT 'e.g., kg, liter, piece, pack',
    stock_quantity INT DEFAULT 0,
    min_order_quantity INT DEFAULT 1,
    max_order_quantity INT DEFAULT 10,
    rating DECIMAL(2, 1) DEFAULT 0.0 COMMENT 'Average rating out of 5',
    total_reviews INT DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE COMMENT 'Featured products shown on home screen',
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_category_id (category_id),
    INDEX idx_is_featured (is_featured),
    INDEX idx_is_available (is_available),
    INDEX idx_price (price),
    INDEX idx_rating (rating),
    FULLTEXT idx_search (name, description, brand)
) ENGINE=InnoDB COMMENT='Product catalog';

-- ============================================================================
-- TABLE: addresses
-- Description: User delivery addresses
-- ============================================================================
CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    address_type ENUM('HOME', 'WORK', 'OTHER') DEFAULT 'HOME',
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_default (is_default)
) ENGINE=InnoDB COMMENT='User delivery addresses';

-- ============================================================================
-- TABLE: cart_items
-- Description: User shopping cart items
-- ============================================================================
CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB COMMENT='User shopping cart';

-- ============================================================================
-- TABLE: orders
-- Description: Order header information
-- ============================================================================
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE COMMENT 'Display order number like ORD123456',
    user_id INT NOT NULL,
    address_id INT NOT NULL,
    delivery_address TEXT NOT NULL COMMENT 'Snapshot of delivery address at time of order',
    payment_method ENUM('COD', 'CARD', 'UPI', 'WALLET') DEFAULT 'COD',
    subtotal DECIMAL(10, 2) NOT NULL,
    delivery_fee DECIMAL(10, 2) DEFAULT 0.00,
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status ENUM('PLACED', 'CONFIRMED', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED') DEFAULT 'PLACED',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estimated_delivery TIMESTAMP NULL DEFAULT NULL,
    delivered_at TIMESTAMP NULL DEFAULT NULL,
    cancelled_at TIMESTAMP NULL DEFAULT NULL,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE RESTRICT,
    INDEX idx_order_number (order_number),
    INDEX idx_user_id (user_id),
    INDEX idx_order_status (order_status),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB COMMENT='Order header information';

-- ============================================================================
-- TABLE: order_items
-- Description: Individual items in each order
-- ============================================================================
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(200) NOT NULL COMMENT 'Snapshot of product name',
    product_image VARCHAR(255),
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL COMMENT 'Price per unit at time of order',
    total_price DECIMAL(10, 2) NOT NULL COMMENT 'quantity * unit_price',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB COMMENT='Order line items';

-- ============================================================================
-- TABLE: order_status_history
-- Description: Tracks order status changes for timeline display
-- ============================================================================
CREATE TABLE order_status_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    status ENUM('PLACED', 'CONFIRMED', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED') NOT NULL,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_status (status)
) ENGINE=InnoDB COMMENT='Order status change history for tracking';

-- ============================================================================
-- TABLE: product_reviews (Optional - for future enhancement)
-- Description: Customer reviews and ratings for products
-- ============================================================================
CREATE TABLE product_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL COMMENT 'User can only review purchased products',
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product_order (user_id, product_id, order_id),
    INDEX idx_product_id (product_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB COMMENT='Product reviews and ratings';

-- ============================================================================
-- SAMPLE DATA INSERTION
-- ============================================================================

-- Insert Categories
INSERT INTO categories (name, description, image_url, display_order) VALUES
('Vegetables & Fruits', 'Fresh vegetables and fruits', 'https://images.unsplash.com/photo-1610348725531-843dff563e2c?w=400', 1),
('Dairy & Breakfast', 'Milk, bread, eggs and breakfast items', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400', 2),
('Snacks & Munchies', 'Chips, namkeen, biscuits and snacks', 'https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=400', 3),
('Beverages', 'Cold drinks, juices and beverages', 'https://images.unsplash.com/photo-1437418747212-8d9709afab22?w=400', 4),
('Bakery & Biscuits', 'Bread, buns, cakes and cookies', 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400', 5),
('Instant & Frozen Food', 'Ready to eat and frozen items', 'https://images.unsplash.com/photo-1476124369491-a7adeb25e6f6?w=400', 6),
('Personal Care', 'Bath, beauty and hygiene products', 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400', 7),
('Home & Kitchen', 'Kitchen essentials and home care', 'https://images.unsplash.com/photo-1556911220-bff31c812dba?w=400', 8);

-- Insert Products for Vegetables & Fruits
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(1, 'Fresh Tomatoes', 'Farm fresh red tomatoes', 'Farm Fresh', 'https://images.unsplash.com/photo-1546470427-227e20c90299?w=400', 40.00, 50.00, 20, 'kg', 100, 4.5, 120, TRUE),
(1, 'Onions', 'Premium quality onions', 'Local', 'https://images.unsplash.com/photo-1618512496248-a07fe83aa8cb?w=400', 35.00, NULL, 0, 'kg', 150, 4.2, 89, FALSE),
(1, 'Fresh Bananas', 'Ripe yellow bananas', 'Farm Fresh', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400', 50.00, 60.00, 17, 'dozen', 80, 4.7, 200, TRUE),
(1, 'Green Capsicum', 'Fresh bell peppers', 'Farm Fresh', 'https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?w=400', 60.00, NULL, 0, 'kg', 60, 4.3, 45, FALSE),
(1, 'Fresh Carrots', 'Crunchy orange carrots', 'Organic Farm', 'https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=400', 45.00, NULL, 0, 'kg', 90, 4.4, 67, FALSE);

-- Insert Products for Dairy & Breakfast
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(2, 'Amul Taaza Milk', 'Toned fresh milk 1L', 'Amul', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400', 56.00, NULL, 0, 'liter', 200, 4.6, 350, TRUE),
(2, 'Britannia Bread', 'Whole wheat sandwich bread', 'Britannia', 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400', 40.00, 45.00, 11, 'pack', 120, 4.4, 180, FALSE),
(2, 'Amul Butter', 'Salted table butter 100g', 'Amul', 'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=400', 54.00, NULL, 0, 'pack', 150, 4.8, 420, TRUE),
(2, 'Farm Fresh Eggs', 'Brown eggs pack of 6', 'Farm Fresh', 'https://images.unsplash.com/photo-1582722872445-44dc5f7e3c8f?w=400', 36.00, 40.00, 10, 'pack', 100, 4.5, 290, FALSE),
(2, 'Mother Dairy Curd', 'Fresh dahi 400g cup', 'Mother Dairy', 'https://images.unsplash.com/photo-1571212515416-fef01fc43637?w=400', 30.00, NULL, 0, 'cup', 80, 4.3, 156, FALSE);

-- Insert Products for Snacks & Munchies
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(3, 'Lays Classic Salted', 'Crispy potato chips 52g', 'Lays', 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=400', 20.00, NULL, 0, 'pack', 250, 4.6, 580, TRUE),
(3, 'Kurkure Masala Munch', 'Spicy corn puffs 85g', 'Kurkure', 'https://images.unsplash.com/photo-1613919234527-3f9a1f0e2e92?w=400', 20.00, 25.00, 20, 'pack', 180, 4.5, 420, FALSE),
(3, 'Haldirams Bhujia', 'Crispy gram flour snack 400g', 'Haldirams', 'https://images.unsplash.com/photo-1599490659213-e2b9527bd087?w=400', 80.00, 90.00, 11, 'pack', 90, 4.7, 320, TRUE),
(3, 'Parle Hide & Seek', 'Chocolate chip cookies 120g', 'Parle', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400', 30.00, NULL, 0, 'pack', 200, 4.4, 280, FALSE);

-- Insert Products for Beverages
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(4, 'Coca Cola', 'Chilled cold drink 750ml', 'Coca Cola', 'https://images.unsplash.com/photo-1554866585-cd94860890b7?w=400', 40.00, NULL, 0, 'bottle', 300, 4.5, 650, TRUE),
(4, 'Tropicana Orange Juice', '100% orange juice 1L', 'Tropicana', 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400', 110.00, 130.00, 15, 'pack', 80, 4.6, 290, TRUE),
(4, 'Red Bull Energy Drink', 'Energy drink 250ml', 'Red Bull', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=400', 125.00, NULL, 0, 'can', 100, 4.3, 180, FALSE),
(4, 'Bisleri Water', 'Packaged drinking water 1L', 'Bisleri', 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=400', 20.00, NULL, 0, 'bottle', 500, 4.7, 890, FALSE);

-- Insert Products for Bakery & Biscuits
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(5, 'Harvest Gold Bread', 'White sandwich bread', 'Harvest Gold', 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400', 38.00, 42.00, 10, 'pack', 100, 4.3, 150, FALSE),
(5, 'Britannia Good Day', 'Butter cookies 100g', 'Britannia', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400', 30.00, NULL, 0, 'pack', 180, 4.5, 340, TRUE),
(5, 'English Oven Cake', 'Chocolate cake slice', 'English Oven', 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400', 60.00, 70.00, 14, 'piece', 50, 4.4, 95, FALSE);

-- Insert Products for Instant & Frozen Food
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(6, 'Maggi Noodles', 'Masala instant noodles 4-pack', 'Maggi', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90?w=400', 52.00, 56.00, 7, 'pack', 250, 4.7, 980, TRUE),
(6, 'McCain French Fries', 'Crispy frozen fries 420g', 'McCain', 'https://images.unsplash.com/photo-1630384662332-58a6625a315f?w=400', 125.00, 150.00, 17, 'pack', 60, 4.4, 210, FALSE),
(6, 'ID Fresh Idli Batter', 'Ready to cook 1kg', 'ID Fresh', 'https://images.unsplash.com/photo-1589301773859-bb024d3ad558?w=400', 65.00, NULL, 0, 'pack', 70, 4.6, 180, FALSE);

-- Insert Products for Personal Care
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(7, 'Colgate Toothpaste', 'MaxFresh gel 150g', 'Colgate', 'https://images.unsplash.com/photo-1622597467836-f3285f2131b8?w=400', 85.00, 95.00, 11, 'pack', 150, 4.5, 420, FALSE),
(7, 'Dove Soap', 'Moisturizing beauty bar 75g', 'Dove', 'https://images.unsplash.com/photo-1585128792160-78e0b4abf86d?w=400', 45.00, NULL, 0, 'piece', 200, 4.6, 560, TRUE),
(7, 'Head & Shoulders Shampoo', 'Anti-dandruff 180ml', 'Head & Shoulders', 'https://images.unsplash.com/photo-1535585209827-a15fcdbc4c2d?w=400', 220.00, 250.00, 12, 'bottle', 90, 4.4, 290, FALSE);

-- Insert Products for Home & Kitchen
INSERT INTO products (category_id, name, description, brand, image_url, price, original_price, discount_percentage, unit, stock_quantity, rating, total_reviews, is_featured) VALUES
(8, 'Vim Dishwash Bar', 'Lemon dishwash bar 300g', 'Vim', 'https://images.unsplash.com/photo-1563453392212-326f5e854473?w=400', 25.00, NULL, 0, 'piece', 180, 4.3, 350, FALSE),
(8, 'Lizol Disinfectant', 'Floor cleaner 500ml', 'Lizol', 'https://images.unsplash.com/photo-1585421514738-01798e348b17?w=400', 95.00, 110.00, 14, 'bottle', 120, 4.5, 280, FALSE),
(8, 'Surf Excel Detergent', 'Matic front load 1kg', 'Surf Excel', 'https://images.unsplash.com/photo-1602526212525-a285191d4b47?w=400', 185.00, 210.00, 12, 'pack', 100, 4.6, 450, TRUE);

-- Insert Sample User (password: Test@123 - hashed using bcrypt)
INSERT INTO users (name, email, password_hash, phone) VALUES
('Test User', 'test@example.com', '$2b$10$YourHashedPasswordHere', '9876543210'),
('John Doe', 'john@example.com', '$2b$10$YourHashedPasswordHere', '9876543211'),
('Jane Smith', 'jane@example.com', '$2b$10$YourHashedPasswordHere', '9876543212');

-- Insert Sample Addresses
INSERT INTO addresses (user_id, full_name, phone, address_line1, address_line2, city, state, pincode, address_type, is_default) VALUES
(1, 'Test User', '9876543210', '123 Main Street', 'Near City Mall', 'Mumbai', 'Maharashtra', '400001', 'HOME', TRUE),
(1, 'Test User', '9876543210', '456 Office Tower', 'Floor 5', 'Mumbai', 'Maharashtra', '400002', 'WORK', FALSE),
(2, 'John Doe', '9876543211', '789 Park Avenue', 'Apartment 12B', 'Delhi', 'Delhi', '110001', 'HOME', TRUE);

-- Insert Sample Cart Items
INSERT INTO cart_items (user_id, product_id, quantity) VALUES
(1, 1, 2),  -- 2kg Tomatoes
(1, 6, 1),  -- 1L Milk
(1, 11, 3); -- 3 packs of Lays

-- Insert Sample Order
INSERT INTO orders (order_number, user_id, address_id, delivery_address, payment_method, subtotal, delivery_fee, total_amount, order_status, estimated_delivery) VALUES
('ORD100001', 1, 1, '123 Main Street, Near City Mall, Mumbai, Maharashtra - 400001', 'COD', 250.00, 20.00, 270.00, 'DELIVERED', DATE_ADD(NOW(), INTERVAL 2 HOUR));

-- Insert Order Items for above order
INSERT INTO order_items (order_id, product_id, product_name, product_image, quantity, unit_price, total_price) VALUES
(1, 1, 'Fresh Tomatoes', 'https://images.unsplash.com/photo-1546470427-227e20c90299?w=400', 2, 40.00, 80.00),
(1, 6, 'Amul Taaza Milk', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400', 2, 56.00, 112.00),
(1, 11, 'Lays Classic Salted', 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=400', 3, 20.00, 60.00);

-- Insert Order Status History
INSERT INTO order_status_history (order_id, status, remarks) VALUES
(1, 'PLACED', 'Order received successfully'),
(1, 'CONFIRMED', 'Order confirmed by store'),
(1, 'PACKED', 'Your order has been packed'),
(1, 'OUT_FOR_DELIVERY', 'Delivery partner on the way'),
(1, 'DELIVERED', 'Order delivered successfully');

-- Insert Sample Active Order
INSERT INTO orders (order_number, user_id, address_id, delivery_address, payment_method, subtotal, delivery_fee, total_amount, order_status, estimated_delivery) VALUES
('ORD100002', 1, 1, '123 Main Street, Near City Mall, Mumbai, Maharashtra - 400001', 'COD', 180.00, 20.00, 200.00, 'OUT_FOR_DELIVERY', DATE_ADD(NOW(), INTERVAL 30 MINUTE));

INSERT INTO order_items (order_id, product_id, product_name, product_image, quantity, unit_price, total_price) VALUES
(2, 3, 'Fresh Bananas', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400', 1, 50.00, 50.00),
(2, 8, 'Amul Butter', 'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=400', 1, 54.00, 54.00),
(2, 15, 'Coca Cola', 'https://images.unsplash.com/photo-1554866585-cd94860890b7?w=400', 2, 40.00, 80.00);

INSERT INTO order_status_history (order_id, status, remarks) VALUES
(2, 'PLACED', 'Order received successfully'),
(2, 'CONFIRMED', 'Order confirmed by store'),
(2, 'PACKED', 'Your order has been packed'),
(2, 'OUT_FOR_DELIVERY', 'Delivery partner on the way - ETA 30 mins');

-- ============================================================================
-- USEFUL QUERIES FOR TESTING
-- ============================================================================

-- Get all products with category name
-- SELECT p.*, c.name as category_name FROM products p JOIN categories c ON p.category_id = c.id;

-- Get user's cart with product details
-- SELECT ci.*, p.name, p.price, p.image_url, (ci.quantity * p.price) as item_total
-- FROM cart_items ci JOIN products p ON ci.product_id = p.id WHERE ci.user_id = 1;

-- Get order with items
-- SELECT o.*, oi.product_name, oi.quantity, oi.unit_price, oi.total_price
-- FROM orders o JOIN order_items oi ON o.id = oi.order_id WHERE o.id = 1;

-- Get order tracking timeline
-- SELECT * FROM order_status_history WHERE order_id = 2 ORDER BY created_at ASC;

-- Search products
-- SELECT * FROM products WHERE MATCH(name, description, brand) AGAINST('milk' IN NATURAL LANGUAGE MODE);

-- Get featured products
-- SELECT * FROM products WHERE is_featured = TRUE AND is_available = TRUE;

-- Get products by category
-- SELECT * FROM products WHERE category_id = 1 AND is_available = TRUE ORDER BY rating DESC;

-- ============================================================================
-- END OF SCHEMA
-- ============================================================================