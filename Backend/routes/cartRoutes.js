const express = require('express');
const router = express.Router();
const {
  getCart,
  addToCart,
  updateCartItem,
  removeCartItem,
  clearCart
} = require('../controllers/cartController');
const { authenticateToken } = require('../middleware/authMiddleware');

// All cart operations require authentication
router.use(authenticateToken);

router.get('/', getCart);
router.post('/add', addToCart);
router.put('/update/:cartItemId', updateCartItem);
router.delete('/remove/:cartItemId', removeCartItem);
router.delete('/clear', clearCart);

module.exports = router;
