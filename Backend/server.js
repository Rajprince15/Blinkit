const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { testConnection } = require('./config/database');
require('dotenv').config();

// Initialize Express app
const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Import Routes
const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const categoryRoutes = require('./routes/categoryRoutes');
const productRoutes = require('./routes/productRoutes');
const cartRoutes = require('./routes/cartRoutes');
const orderRoutes = require('./routes/orderRoutes');
const addressRoutes = require('./routes/addressRoutes');

// API Routes
app.use('/api/auth', authRoutes);
app.use('/api/users', userRoutes);
app.use('/api/categories', categoryRoutes);
app.use('/api/products', productRoutes);
app.use('/api/cart', cartRoutes);
app.use('/api/orders', orderRoutes);
app.use('/api/addresses', addressRoutes);

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    success: true,
    message: 'Blinkit Backend API is running!',
    version: '1.0.0',
    endpoints: {
      auth: '/api/auth',
      users: '/api/users',
      categories: '/api/categories',
      products: '/api/products'
    }
  });
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({
    success: true,
    status: 'healthy',
    timestamp: new Date().toISOString()
  });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({
    success: false,
    message: 'Endpoint not found'
  });
});

// Error handling middleware
app.use((err, req, res, next) => {
  console.error('Server error:', err);
  res.status(500).json({
    success: false,
    message: 'Internal server error'
  });
});

// Start server and test database connection
const startServer = async () => {
  try {
    // Test database connection
    const dbConnected = await testConnection();

    if (!dbConnected) {
      console.error('⚠️  Warning: Could not connect to database. Please check your MySQL server and credentials.');
      console.log('\n📝 To fix this:');
      console.log('   1. Make sure MySQL is running');
      console.log('   2. Check database credentials in .env file');
      console.log('   3. Run the database_schema.sql file to create the database\n');
    }

    // Start listening
    app.listen(PORT, () => {
      console.log('\n' + '='.repeat(60));
      console.log(`🚀 Blinkit Backend Server Started`);
      console.log('='.repeat(60));
      console.log(`📍 Server running on: http://localhost:${PORT}`);
      console.log(`🌍 Environment: ${process.env.NODE_ENV || 'development'}`);
      console.log(`📊 Database: ${process.env.DB_NAME}`);
      console.log('='.repeat(60));
      console.log('\n📚 Available Endpoints:');
      console.log('   POST   /api/auth/signup        - Register new user');
      console.log('   POST   /api/auth/login         - Login user');
      console.log('   GET    /api/auth/profile       - Get user profile (auth required)');
      console.log('   POST   /api/auth/logout        - Logout user (auth required)');
      console.log('   GET    /api/users/profile      - Get user details (auth required)');
      console.log('   PUT    /api/users/profile      - Update user profile (auth required)');
      console.log('   GET    /api/categories         - Get all categories');
      console.log('   GET    /api/products           - Get all products');
      console.log('   GET    /api/products/featured  - Get featured products');
      console.log('   GET    /api/products/search    - Search products');
      console.log('='.repeat(60) + '\n');
    });
  } catch (error) {
    console.error('Failed to start server:', error);
    process.exit(1);
  }
};

// Start the server
startServer();

module.exports = app;
