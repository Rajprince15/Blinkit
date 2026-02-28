const express = require('express');
const router = express.Router();
const { signup, login, getProfile, logout } = require('../controllers/authController');
const { authenticateToken } = require('../middleware/authMiddleware');

/**
 * @route   POST /api/auth/signup
 * @desc    Register a new user
 * @access  Public
 */
router.post('/signup', signup);

/**
 * @route   POST /api/auth/login
 * @desc    Login user
 * @access  Public
 */
router.post('/login', login);

/**
 * @route   GET /api/auth/profile
 * @desc    Get user profile
 * @access  Private (requires authentication)
 */
router.get('/profile', authenticateToken, getProfile);

/**
 * @route   POST /api/auth/logout
 * @desc    Logout user
 * @access  Private
 */
router.post('/logout', authenticateToken, logout);

module.exports = router;
