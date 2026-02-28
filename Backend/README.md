# Blinkit Backend API

Backend server for Blinkit-style grocery delivery application.

## Technology Stack

- **Runtime:** Node.js
- **Framework:** Express.js
- **Database:** MySQL 8.0
- **Authentication:** JWT (JSON Web Tokens)
- **Password Hashing:** bcrypt

## Prerequisites

Before running this backend, make sure you have:

1. **Node.js** (v18 or higher)
   ```bash
   node --version
   npm --version
   ```

2. **MySQL 8.0**
   ```bash
   mysql --version
   ```

## Installation

### 1. Install Dependencies

```bash
cd backend
npm install
```

### 2. Configure Environment Variables

The `.env` file is already created. Update these values if needed:

```env
# Server Configuration
PORT=5000

# Database Configuration
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=your_mysql_password
DB_NAME=blinkit_db
DB_PORT=3306

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_change_this_in_production
JWT_EXPIRES_IN=7d
```

### 3. Set Up Database

**Step 1:** Start MySQL server

**Step 2:** Create database and import schema

```bash
mysql -u root -p
```

Then in MySQL prompt:

```sql
CREATE DATABASE blinkit_db;
USE blinkit_db;
SOURCE /path/to/database_schema.sql;
```

Or use this command directly:

```bash
mysql -u root -p < ../database_schema.sql
```

### 4. Run the Server

**Development mode (with auto-reload):**
```bash
npm run dev
```

**Production mode:**
```bash
npm start
```

Server will start on: `http://localhost:5000`

## API Endpoints

### Authentication APIs

#### 1. Signup
```http
POST /api/auth/signup
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Test@123",
  "phone": "9876543210"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully.",
  "data": {
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

#### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "Test@123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful.",
  "data": {
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

#### 3. Get Profile (Protected)
```http
GET /api/auth/profile
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "9876543210",
    "profile_image": null,
    "created_at": "2025-08-15T10:30:00.000Z",
    "last_login": "2025-08-15T12:00:00.000Z"
  }
}
```

#### 4. Logout
```http
POST /api/auth/logout
Authorization: Bearer YOUR_JWT_TOKEN
```

### User Management APIs

#### 1. Get User Profile
```http
GET /api/users/profile
Authorization: Bearer YOUR_JWT_TOKEN
```

#### 2. Update User Profile
```http
PUT /api/users/profile
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "name": "John Updated",
  "phone": "9999999999"
}
```

## Testing with cURL

### Test Signup
```bash
curl -X POST http://localhost:5000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "Test@123",
    "phone": "9876543210"
  }'
```

### Test Login
```bash
curl -X POST http://localhost:5000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@123"
  }'
```

### Test Profile (Replace TOKEN with actual JWT)
```bash
curl -X GET http://localhost:5000/api/auth/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## Error Handling

All API responses follow this structure:

**Success Response:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Error description"
}
```

## Common HTTP Status Codes

- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized (invalid credentials)
- `403` - Forbidden (invalid token)
- `404` - Not Found
- `409` - Conflict (duplicate entry)
- `500` - Server Error

## Project Structure

```
backend/
├── config/
│   └── database.js          # MySQL connection configuration
├── controllers/
│   ├── authController.js    # Authentication logic
│   └── userController.js    # User management logic
├── middleware/
│   └── authMiddleware.js    # JWT authentication middleware
├── models/
│   └── db.js                # Database helper functions
├── routes/
│   ├── authRoutes.js        # Authentication routes
│   └── userRoutes.js        # User routes
├── .env                     # Environment variables
├── server.js                # Main server file
├── package.json             # Dependencies
└── README.md                # This file
```

## Security Notes

1. **Change JWT Secret:** Update `JWT_SECRET` in `.env` before deploying to production
2. **Password Requirements:** Minimum 6 characters (enforced in controller)
3. **Password Storage:** Passwords are hashed using bcrypt with 10 salt rounds
4. **Token Expiry:** JWT tokens expire after 7 days (configurable)

## Troubleshooting

### Database Connection Failed

1. Check if MySQL is running:
   ```bash
   sudo service mysql status
   ```

2. Verify credentials in `.env` file

3. Make sure database exists:
   ```bash
   mysql -u root -p -e "SHOW DATABASES;"
   ```

### Port Already in Use

Change the `PORT` in `.env` file to another port (e.g., 5001)

### Dependencies Installation Failed

Try clearing npm cache:
```bash
npm cache clean --force
npm install
```

## Next Steps

Phase 1 Complete! Next:
- Phase 2: Add product APIs (categories, products, search)
- Phase 3: Implement cart functionality
- Phase 4: Add order management

---

**Developed by E1 Agent**
