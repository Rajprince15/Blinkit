const express = require('express');
const router = express.Router();
const {
  createOrder,
  getOrders,
  getOrderById,
  trackOrder
} = require('../controllers/orderController');
const { authenticateToken } = require('../middleware/authMiddleware');

router.use(authenticateToken);

router.post('/create', createOrder);
router.get('/', getOrders);
router.get('/:orderId', getOrderById);
router.get('/:orderId/track', trackOrder);

module.exports = router;
