# Blinkit Clone - Backend API

Node.js + Express.js backend for the Blinkit Clone grocery delivery application.

## 🚀 Features

- RESTful API architecture
- JWT-based authentication
- MySQL database integration
- Password hashing with bcrypt
- CORS enabled for cross-origin requests
- Request logging and error handling
- Admin middleware for protected routes

## 📋 Prerequisites

- Node.js v18 or higher
- MySQL 8.0
- npm or yarn

## ⚙️ Installation

1. **Navigate to backend directory**
   ```bash
   cd Backend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Setup MySQL Database**
   ```bash
   mysql -u root -p
   CREATE DATABASE blinkit_db;
   USE blinkit_db;
   SOURCE ../database.sql
   ```

4. **Configure Environment Variables**
   
   Create a `.env` file in the Backend directory:
   ```env
   PORT=5000
   DB_HOST=localhost
   DB_USER=root
   DB_PASSWORD=your_mysql_password
   DB_NAME=blinkit_db
   JWT_SECRET=your_super_secret_jwt_key_here_change_in_production
   ```

5. **Run the server**
   ```bash
   npm start
   ```

   The server will start on `http://localhost:5000`

## 📚 API Documentation

### Base URL
```
http://localhost:5000/api
```

### Authentication

All authenticated endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 🔐 Authentication APIs

### 1. User Signup
**POST** `/auth/signup`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "1234567890"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com"
    }
  }
}
```

### 2. User Login
**POST** `/auth/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com"
    }
  }
}
```

### 3. Get Profile
**GET** `/auth/profile` 🔒

**Headers:**
```
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890"
  }
}
```

### 4. Logout
**POST** `/auth/logout` 🔒

---

## 📦 Product APIs

### 1. Get All Categories
**GET** `/categories`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Fruits & Vegetables",
      "image": "fruits.jpg",
      "icon": "🥬"
    }
  ]
}
```

### 2. Get All Products
**GET** `/products?page=1&limit=20`

### 3. Get Featured Products
**GET** `/products/featured?limit=10`

### 4. Search Products
**GET** `/products/search?q=milk`

### 5. Get Products by Category
**GET** `/products/category/:categoryId`

### 6. Get Product Details
**GET** `/products/:productId`

---

## 🛒 Cart APIs

All cart APIs require authentication 🔒

### 1. Get Cart
**GET** `/cart`

**Response:**
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 1,
        "productId": 5,
        "productName": "Fresh Milk",
        "price": 50.00,
        "quantity": 2,
        "image": "milk.jpg"
      }
    ],
    "subtotal": 100.00,
    "totalItems": 2
  }
}
```

### 2. Add to Cart
**POST** `/cart/add`

**Request Body:**
```json
{
  "productId": 5,
  "quantity": 2
}
```

### 3. Update Cart Item
**PUT** `/cart/update/:cartItemId`

**Request Body:**
```json
{
  "quantity": 3
}
```

### 4. Remove from Cart
**DELETE** `/cart/remove/:cartItemId`

### 5. Clear Cart
**DELETE** `/cart/clear`

---

## 📍 Address APIs

All address APIs require authentication 🔒

### 1. Get All Addresses
**GET** `/addresses`

### 2. Add Address
**POST** `/addresses`

**Request Body:**
```json
{
  "fullName": "John Doe",
  "phoneNumber": "1234567890",
  "addressLine1": "123 Main St",
  "addressLine2": "Apt 4B",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pinCode": "400001",
  "addressType": "HOME"
}
```

### 3. Update Address
**PUT** `/addresses/:addressId`

### 4. Delete Address
**DELETE** `/addresses/:addressId`

### 5. Set Default Address
**PUT** `/addresses/:addressId/default`

---

## 📋 Order APIs

All order APIs require authentication 🔒

### 1. Create Order
**POST** `/orders/create`

**Request Body:**
```json
{
  "addressId": 3,
  "deliveryAddress": "123 Main St, Mumbai, Maharashtra, 400001",
  "paymentMethod": "COD",
  "items": [
    {
      "productId": 5,
      "productName": "Fresh Milk",
      "quantity": 2,
      "price": 50.00,
      "productImage": "milk.jpg"
    }
  ],
  "totalAmount": 100.00
}
```

**Response:**
```json
{
  "success": true,
  "message": "Order placed",
  "orderId": 15,
  "orderNumber": "ORD1672934567890"
}
```

### 2. Get All Orders
**GET** `/orders`

**Response:**
```json
{
  "success": true,
  "count": 5,
  "data": [
    {
      "id": 15,
      "orderNumber": "ORD1672934567890",
      "totalAmount": 100.00,
      "orderStatus": "PLACED",
      "orderDate": "2025-08-15T10:30:00.000Z"
    }
  ]
}
```

### 3. Get Order Details
**GET** `/orders/:orderId`

**Response:**
```json
{
  "success": true,
  "data": {
    "order": {
      "id": 15,
      "orderNumber": "ORD1672934567890",
      "totalAmount": 100.00,
      "orderStatus": "OUT_FOR_DELIVERY",
      "deliveryAddress": "123 Main St, Mumbai"
    },
    "items": [
      {
        "id": 1,
        "productName": "Fresh Milk",
        "quantity": 2,
        "unitPrice": 50.00,
        "totalPrice": 100.00
      }
    ]
  }
}
```

### 4. Track Order
**GET** `/orders/:orderId/track`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "orderId": 15,
      "status": "PLACED",
      "remarks": "Order received successfully",
      "createdAt": "2025-08-15T10:30:00.000Z"
    },
    {
      "id": 2,
      "orderId": 15,
      "status": "CONFIRMED",
      "remarks": "Order confirmed by store",
      "createdAt": "2025-08-15T10:45:00.000Z"
    }
  ]
}
```

### 5. Update Order Status (Admin Only)
**PUT** `/orders/:orderId/status` 🔒👨‍💼

**Request Body:**
```json
{
  "status": "CONFIRMED",
  "remarks": "Order confirmed by store"
}
```

**Valid Status Values:**
- `PLACED`
- `CONFIRMED`
- `PACKED`
- `OUT_FOR_DELIVERY`
- `DELIVERED`
- `CANCELLED`

---

## 🔐 Middleware

### authenticateToken
Verifies JWT token for protected routes.

### authenticateAdmin
Checks if user has admin privileges.
- Admin users have email containing "admin" or role = "admin"

---

## 🗄️ Database Schema

The database includes the following tables:
- `users` - User accounts
- `categories` - Product categories
- `products` - Product catalog
- `cart_items` - Shopping cart
- `addresses` - User addresses
- `orders` - Order headers
- `order_items` - Order line items
- `order_status_history` - Order tracking timeline

See `database.sql` for complete schema.

---

## 🛠️ Development

### Project Structure
```
Backend/
├── server.js              # Main server file
├── package.json           # Dependencies
├── .env                   # Environment variables
├── config/
│   └── database.js        # MySQL connection
├── controllers/
│   ├── authController.js
│   ├── productController.js
│   ├── cartController.js
│   ├── addressController.js
│   └── orderController.js
├── routes/
│   ├── authRoutes.js
│   ├── productRoutes.js
│   ├── cartRoutes.js
│   ├── addressRoutes.js
│   └── orderRoutes.js
├── middleware/
│   └── authMiddleware.js
└── models/
    └── db.js              # Database helpers
```

### Running in Development
```bash
npm run dev  # If nodemon is configured
```

### Testing APIs
Use Postman, curl, or any API testing tool.

Example with curl:
```bash
# Login
curl -X POST http://localhost:5000/api/auth/login 
  -H "Content-Type: application/json" 
  -d '{"email":"test@example.com","password":"password123"}'

# Get products (with token)
curl http://localhost:5000/api/products 
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 🐛 Troubleshooting

### Database Connection Issues
- Verify MySQL is running: `sudo systemctl status mysql`
- Check credentials in `.env`
- Ensure database exists: `SHOW DATABASES;`

### Port Already in Use
```bash
# Find process using port 5000
lsof -i :5000
# Kill the process
kill -9 <PID>
```

### JWT Secret Error
- Make sure `JWT_SECRET` is set in `.env`
- Use a strong, random string for production

---

## 📝 Notes

- Change `JWT_SECRET` in production
- Use HTTPS in production
- Implement rate limiting for production
- Add input sanitization
- Configure proper CORS origins for production

---

## 🤝 Contributing

Contributions are welcome! Please follow coding standards and test thoroughly.

---

**Backend API Documentation - Blinkit Clone**
