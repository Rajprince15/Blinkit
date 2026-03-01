const jwt = require('jsonwebtoken');
require('dotenv').config();

/**
 * Middleware to verify JWT token and authenticate requests
 */
const authenticateToken = (req, res, next) => {
  try {
    // Get token from header
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Bearer TOKEN

    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Access denied. No token provided.'
      });
    }

    // Verify token
    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
      if (err) {
        return res.status(403).json({
          success: false,
          message: 'Invalid or expired token.'
        });
      }

      // Attach user info to request
      req.user = decoded;
      next();
    });
  } catch (error) {
    console.error('Auth middleware error:', error);
    return res.status(500).json({
      success: false,
      message: 'Authentication failed.'
    });
  }
};

/**
 * Middleware to check if user is admin
 * Note: For demo purposes, admin users have role 'admin' or email contains 'admin'
 * In production, implement proper role-based access control
 */
const authenticateAdmin = (req, res, next) => {
  try {
    // First authenticate the token
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'Access denied. No token provided.'
      });
    }

    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
      if (err) {
        return res.status(403).json({
          success: false,
          message: 'Invalid or expired token.'
        });
      }

      // Check if user is admin
      // For demo: check if email contains 'admin' or role is 'admin'
      const isAdmin = decoded.email?.toLowerCase().includes('admin') || decoded.role === 'admin';
      
      if (!isAdmin) {
        return res.status(403).json({
          success: false,
          message: 'Access denied. Admin privileges required.'
        });
      }

      req.user = decoded;
      next();
    });
  } catch (error) {
    console.error('Admin auth middleware error:', error);
    return res.status(500).json({
      success: false,
      message: 'Authentication failed.'
    });
  }
};

module.exports = { authenticateToken, authenticateAdmin };
