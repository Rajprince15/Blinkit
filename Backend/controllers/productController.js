const Database = require('../models/db');

/**
 * Get All Products with Pagination
 */
const getAllProducts = async (req, res) => {
  try {
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 20;
    const offset = (page - 1) * limit;

    const products = await Database.query(
      `SELECT p.*, c.name as category_name
       FROM products p
       JOIN categories c ON p.category_id = c.id
       WHERE p.is_available = TRUE
       ORDER BY p.created_at DESC
       LIMIT ? OFFSET ?`,
      [limit, offset]
    );

    // Get total count
    const countResult = await Database.getOne(
      'SELECT COUNT(*) as total FROM products WHERE is_available = TRUE'
    );

    res.status(200).json({
      success: true,
      count: products.length,
      total: countResult.total,
      page: page,
      totalPages: Math.ceil(countResult.total / limit),
      data: products
    });
  } catch (error) {
    console.error('Get all products error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching products.'
    });
  }
};

/**
 * Get Products by Category
 */
const getProductsByCategory = async (req, res) => {
  try {
    const { categoryId } = req.params;
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 20;
    const offset = (page - 1) * limit;

    const products = await Database.query(
      `SELECT p.*, c.name as category_name
       FROM products p
       JOIN categories c ON p.category_id = c.id
       WHERE p.category_id = ? AND p.is_available = TRUE
       ORDER BY p.rating DESC
       LIMIT ? OFFSET ?`,
      [categoryId, limit, offset]
    );

    res.status(200).json({
      success: true,
      count: products.length,
      data: products
    });
  } catch (error) {
    console.error('Get products by category error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching products by category.'
    });
  }
};

/**
 * Get Product by ID
 */
const getProductById = async (req, res) => {
  try {
    const { productId } = req.params;

    const product = await Database.getOne(
      `SELECT p.*, c.name as category_name
       FROM products p
       JOIN categories c ON p.category_id = c.id
       WHERE p.id = ? AND p.is_available = TRUE`,
      [productId]
    );

    if (!product) {
      return res.status(404).json({
        success: false,
        message: 'Product not found.'
      });
    }

    res.status(200).json({
      success: true,
      data: product
    });
  } catch (error) {
    console.error('Get product by ID error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching product.'
    });
  }
};

/**
 * Search Products
 */
const searchProducts = async (req, res) => {
  try {
    const { q } = req.query; // search query

    if (!q || q.trim() === '') {
      return res.status(400).json({
        success: false,
        message: 'Search query is required.'
      });
    }

    const searchTerm = `%${q}%`;

    const products = await Database.query(
      `SELECT p.*, c.name as category_name
       FROM products p
       JOIN categories c ON p.category_id = c.id
       WHERE p.is_available = TRUE
       AND (p.name LIKE ? OR p.description LIKE ? OR p.brand LIKE ?)
       ORDER BY p.rating DESC
       LIMIT 50`,
      [searchTerm, searchTerm, searchTerm]
    );

    res.status(200).json({
      success: true,
      count: products.length,
      data: products
    });
  } catch (error) {
    console.error('Search products error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error searching products.'
    });
  }
};

/**
 * Get Featured Products
 */
const getFeaturedProducts = async (req, res) => {
  try {
    const limit = parseInt(req.query.limit) || 10;

    const products = await Database.query(
      `SELECT p.*, c.name as category_name
       FROM products p
       JOIN categories c ON p.category_id = c.id
       WHERE p.is_featured = TRUE AND p.is_available = TRUE
       ORDER BY p.rating DESC
       LIMIT ?`,
      [limit]
    );

    res.status(200).json({
      success: true,
      count: products.length,
      data: products
    });
  } catch (error) {
    console.error('Get featured products error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching featured products.'
    });
  }
};

module.exports = {
  getAllProducts,
  getProductsByCategory,
  getProductById,
  searchProducts,
  getFeaturedProducts
};
