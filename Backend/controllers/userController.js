const Database = require('../models/db');

/**
 * Get User Profile Details
 */
const getUserProfile = async (req, res) => {
  try {
    const userId = req.user.id;

    const user = await Database.getOne(
      'SELECT id, name, email, phone, profile_image, created_at FROM users WHERE id = ?',
      [userId]
    );

    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User not found.'
      });
    }

    res.status(200).json({
      success: true,
      data: user
    });
  } catch (error) {
    console.error('Get user profile error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error fetching user profile.'
    });
  }
};

/**
 * Update User Profile
 */
const updateUserProfile = async (req, res) => {
  try {
    const userId = req.user.id;
    const { name, phone } = req.body;

    if (!name) {
      return res.status(400).json({
        success: false,
        message: 'Name is required.'
      });
    }

    // Update user profile
    const affectedRows = await Database.update(
      'UPDATE users SET name = ?, phone = ? WHERE id = ?',
      [name, phone || null, userId]
    );

    if (affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: 'User not found.'
      });
    }

    // Get updated user data
    const updatedUser = await Database.getOne(
      'SELECT id, name, email, phone, profile_image FROM users WHERE id = ?',
      [userId]
    );

    res.status(200).json({
      success: true,
      message: 'Profile updated successfully.',
      data: updatedUser
    });
  } catch (error) {
    console.error('Update profile error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error updating profile.'
    });
  }
};

module.exports = {
  getUserProfile,
  updateUserProfile
};
