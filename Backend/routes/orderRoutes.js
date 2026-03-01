const express = require('express');
const router = express.Router();
const {
  createOrder,
  getOrders,
  getOrderById,
  trackOrder,
  updateOrderStatus
} = require('../controllers/orderController');
const { authenticateToken, authenticateAdmin } = require('../middleware/authMiddleware');

// User routes (authenticated)

router.use(authenticateToken);

router.post('/create', createOrder);
router.get('/', getOrders);
router.get('/:orderId', getOrderById);
router.get('/:orderId/track', trackOrder);
// Admin routes
router.put('/:orderId/status', authenticateAdmin, updateOrderStatus);


module.exports = router;
