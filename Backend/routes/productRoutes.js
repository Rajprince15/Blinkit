const express = require('express');
const router = express.Router();
const {
  getAllProducts,
  getProductsByCategory,
  getProductById,
  searchProducts,
  getFeaturedProducts
} = require('../controllers/productController');

/**
 * @route   GET /api/products
 * @desc    Get all products with pagination
 * @access  Public
 */
router.get('/', getAllProducts);

/**
 * @route   GET /api/products/featured
 * @desc    Get featured products
 * @access  Public
 */
router.get('/featured', getFeaturedProducts);

/**
 * @route   GET /api/products/search
 * @desc    Search products
 * @access  Public
 */
router.get('/search', searchProducts);

/**
 * @route   GET /api/products/category/:categoryId
 * @desc    Get products by category
 * @access  Public
 */
router.get('/category/:categoryId', getProductsByCategory);

/**
 * @route   GET /api/products/:productId
 * @desc    Get product by ID
 * @access  Public
 */
router.get('/:productId', getProductById);

module.exports = router;
