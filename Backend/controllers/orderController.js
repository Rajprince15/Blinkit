const Database = require('../models/db');

/**
 * Create a new order
 */
const createOrder = async (req, res) => {
  try {
    const userId = req.user.id;
    // accept camelCase or snake_case
    const addressId = req.body.addressId || req.body.address_id;
    const deliveryAddress = req.body.deliveryAddress || req.body.delivery_address;
    const paymentMethod = req.body.paymentMethod || req.body.payment_method;
    const items = req.body.items || req.body.order_items;
    const totalAmount = req.body.totalAmount || req.body.total_amount;

    if (!addressId || !deliveryAddress || !Array.isArray(items) || items.length === 0 || !totalAmount) {
      return res.status(400).json({ success: false, message: 'Invalid order data' });
    }

    // Generate an order number
    const orderNumber = 'ORD' + Date.now();

    // subtotal calculation from items
    const subtotal = items.reduce((sum, it) => sum + it.price * it.quantity, 0);
    const deliveryFee = 0.0; // can be dynamic
    const discountAmount = 0.0; // can be dynamic

    const insertId = await Database.insert(
      `INSERT INTO orders 
       (order_number, user_id, address_id, delivery_address, payment_method, subtotal, delivery_fee, discount_amount, total_amount, order_status)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'PLACED')`,
      [orderNumber, userId, addressId, deliveryAddress, paymentMethod, subtotal, deliveryFee, discountAmount, totalAmount]
    );

    // Insert order items
    for (const it of items) {
      const pid = it.productId || it.product_id;
      const qty = it.quantity || it.qty || 0;
      const price = it.price || it.unit_price || 0;
      const pname = it.productName || it.product_name || '';
      const pimage = it.productImage || it.product_image || null;

      await Database.insert(
        `INSERT INTO order_items 
         (order_id, product_id, product_name, product_image, quantity, unit_price, total_price)
         VALUES (?, ?, ?, ?, ?, ?, ?)`,
        [insertId, pid, pname, pimage, qty, price, price * qty]
      );
      
    }
    // Insert initial order status into history for tracking
    await Database.insert(
      `INSERT INTO order_status_history (order_id, status, remarks)
       VALUES (?, 'PLACED', 'Order received successfully')`,
      [insertId]
    );

    res.status(201).json({ success: true, message: 'Order placed', orderId: insertId, orderNumber });
  } catch (error) {
    console.error('Create order error:', error);
    res.status(500).json({ success: false, message: 'Server error creating order' });
  }
};

/**
 * Get all orders for current user
 */
const getOrders = async (req, res) => {
  try {
    const userId = req.user.id;
    const orders = await Database.query(
      'SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC',
      [userId]
    );
    res.status(200).json({ success: true, count: orders.length, data: orders });
  } catch (error) {
    console.error('Get orders error:', error);
    res.status(500).json({ success: false, message: 'Server error fetching orders' });
  }
};

/**
 * Get a single order by id (must belong to user)
 */
const getOrderById = async (req, res) => {
  try {
    const userId = req.user.id;
    const { orderId } = req.params;

    const order = await Database.getOne(
      'SELECT * FROM orders WHERE id = ? AND user_id = ?',
      [orderId, userId]
    );
    if (!order) {
      return res.status(404).json({ success: false, message: 'Order not found' });
    }

    const items = await Database.query(
      'SELECT * FROM order_items WHERE order_id = ?',
      [orderId]
    );

    res.status(200).json({ success: true, data: { order, items } });
  } catch (error) {
    console.error('Get order by id error:', error);
    res.status(500).json({ success: false, message: 'Server error fetching order' });
  }
};

/**
 * Get order tracking timeline
 */
const trackOrder = async (req, res) => {
  try {
    const userId = req.user.id;
    const { orderId } = req.params;

    // verify order belongs to user
    const order = await Database.getOne(
      'SELECT id FROM orders WHERE id = ? AND user_id = ?',
      [orderId, userId]
    );
    if (!order) {
      return res.status(404).json({ success: false, message: 'Order not found' });
    }

    const timeline = await Database.query(
      'SELECT status, remarks, created_at FROM order_status_history WHERE order_id = ? ORDER BY created_at ASC',
      [orderId]
    );

    res.status(200).json({ success: true, data: timeline });
  } catch (error) {
    console.error('Track order error:', error);
    res.status(500).json({ success: false, message: 'Server error tracking order' });
  }
};

module.exports = {
  createOrder,
  getOrders,
  getOrderById,
  trackOrder
};
