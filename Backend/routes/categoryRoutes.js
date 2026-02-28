const express = require('express');
const router = express.Router();
const { getAllCategories, getCategoryById } = require('../controllers/categoryController');

/**
 * @route   GET /api/categories
 * @desc    Get all categories
 * @access  Public
 */
router.get('/', getAllCategories);

/**
 * @route   GET /api/categories/:categoryId
 * @desc    Get category by ID
 * @access  Public
 */
router.get('/:categoryId', getCategoryById);

module.exports = router;
