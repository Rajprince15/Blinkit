const Database = require('../models/db');

/**
 * Get All Categories
 */
const getAllCategories = async (req, res) => {
  try {
    const categories = await Database.getAll(
      'SELECT id, name, description, image_url, display_order FROM categories WHERE is_active = TRUE ORDER BY display_order ASC'
    );

    res.status(200).json({
      success: true,
      count: categories.length,
      data: categories
    });
  } catch (error) {
    console.error('Get categories error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching categories.'
    });
  }
};

/**
 * Get Category by ID
 */
const getCategoryById = async (req, res) => {
  try {
    const { categoryId } = req.params;

    const category = await Database.getOne(
      'SELECT id, name, description, image_url, display_order FROM categories WHERE id = ? AND is_active = TRUE',
      [categoryId]
    );

    if (!category) {
      return res.status(404).json({
        success: false,
        message: 'Category not found.'
      });
    }

    res.status(200).json({
      success: true,
      data: category
    });
  } catch (error) {
    console.error('Get category by ID error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching category.'
    });
  }
};

module.exports = {
  getAllCategories,
  getCategoryById
};
