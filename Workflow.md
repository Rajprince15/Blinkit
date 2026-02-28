# Blinkit-Style Grocery Delivery App - Development Workflows

## Project Overview
**App Name:** Blinkit Clone
**Technology Stack:**
- **Mobile:** Android (Kotlin + XML layouts + MVVM)
- **Backend:** Node.js + Express.js
- **Database:** MySQL 8.0
- **Libraries:** Retrofit, Glide, RecyclerView

---

## PHASE 1: Database Schema + Backend Foundation (~5-6 credits)

### Objectives
- Set up MySQL 8.0 database with complete schema
- Initialize Node.js + Express backend project
- Implement user authentication APIs
- Test database connections and basic CRUD operations

### Tasks

#### 1.1 Database Setup
- [ ] Execute database_schema.sql to create all tables
- [ ] Verify table relationships and constraints
- [ ] Insert sample data for testing
- [ ] Tables to create:
    - `users` - User authentication and profile
    - `categories` - Product categories
    - `products` - Product catalog
    - `cart_items` - User shopping cart
    - `orders` - Order header information
    - `order_items` - Order line items
    - `addresses` - User delivery addresses

#### 1.2 Backend Project Structure
```
backend/
├── server.js              # Main Express server
├── package.json           # Dependencies
├── .env                   # Environment variables
├── config/
│   └── database.js        # MySQL connection configuration
├── controllers/
│   ├── authController.js  # Authentication logic
│   └── userController.js  # User management
├── routes/
│   ├── authRoutes.js      # Authentication routes
│   └── userRoutes.js      # User routes
├── middleware/
│   └── authMiddleware.js  # JWT authentication middleware
└── models/
    └── db.js              # Database query helpers
```

#### 1.3 Backend Dependencies
```json
{
  "express": "^4.18.2",
  "mysql2": "^3.6.0",
  "bcrypt": "^5.1.1",
  "jsonwebtoken": "^9.0.2",
  "dotenv": "^16.3.1",
  "cors": "^2.8.5",
  "body-parser": "^1.20.2"
}
```

#### 1.4 API Endpoints - Phase 1

**Authentication APIs**
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/profile` - Get user profile (authenticated)

**User Management APIs**
- `GET /api/users/profile` - Get current user details
- `PUT /api/users/profile` - Update user profile

#### 1.5 Testing Checklist
- [ ] MySQL server running on localhost:3306
- [ ] Backend server running on localhost:5000
- [ ] Test signup API with sample user
- [ ] Test login API and receive JWT token
- [ ] Test authenticated profile endpoint
- [ ] Verify password hashing with bcrypt
- [ ] Test error handling (duplicate email, invalid credentials)

#### 1.6 Deliverables
- Complete MySQL database with schema
- Backend server running with authentication
- Postman/curl test results
- Setup documentation (README.md)

---

## PHASE 2: Products Backend + Android Foundation (~5-6 credits)

### Objectives
- Build product management APIs
- Set up Android project with MVVM architecture
- Convert Jetpack Compose to XML layouts
- Implement authentication screens
- Add theme toggle (Black & White)

### Tasks

#### 2.1 Backend - Product APIs

**API Endpoints**
- `GET /api/categories` - Get all product categories
- `GET /api/products` - Get all products (with pagination)
- `GET /api/products/category/:categoryId` - Get products by category
- `GET /api/products/:productId` - Get product details
- `GET /api/products/search?q=keyword` - Search products
- `GET /api/products/featured` - Get featured products

**Controllers to Create**
- `categoryController.js` - Category operations
- `productController.js` - Product CRUD operations

**Routes to Create**
- `categoryRoutes.js`
- `productRoutes.js`

#### 2.2 Android - Project Structure Setup

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/blinkit/
│   ├── activities/
│   │   ├── MainActivity.kt
│   │   ├── LoginActivity.kt
│   │   ├── SignupActivity.kt
│   │   ├── HomeActivity.kt
│   │   └── SplashActivity.kt
│   ├── adapters/
│   │   ├── CategoryAdapter.kt
│   │   ├── ProductAdapter.kt
│   │   └── BaseAdapter.kt
│   ├── models/
│   │   ├── User.kt
│   │   ├── Category.kt
│   │   ├── Product.kt
│   │   ├── CartItem.kt
│   │   └── Order.kt
│   ├── viewmodels/
│   │   ├── AuthViewModel.kt
│   │   ├── ProductViewModel.kt
│   │   └── BaseViewModel.kt
│   ├── repositories/
│   │   ├── AuthRepository.kt
│   │   └── ProductRepository.kt
│   ├── api/
│   │   ├── ApiClient.kt
│   │   ├── ApiService.kt
│   │   └── ApiResponse.kt
│   └── utils/
│       ├── SharedPrefsManager.kt
│       ├── Constants.kt
│       ├── ThemeManager.kt
│       └── NetworkUtils.kt
└── res/
    ├── layout/
    │   ├── activity_splash.xml
    │   ├── activity_login.xml
    │   ├── activity_signup.xml
    │   └── activity_main.xml
    ├── values/
    │   ├── colors.xml
    │   ├── strings.xml
    │   ├── themes.xml          # Light theme
    │   └── themes_dark.xml     # Dark theme
    └── drawable/
```

#### 2.3 Android - Dependencies (build.gradle.kts)

Add these dependencies:
```kotlin
// Retrofit for networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

// Glide for image loading
implementation("com.github.bumptech.glide:glide:4.16.0")
kapt("com.github.bumptech.glide:compiler:4.16.0")

// RecyclerView
implementation("androidx.recyclerview:recyclerview:1.3.2")

// ViewModel and LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Material Design
implementation("com.google.android.material:material:1.11.0")
```

#### 2.4 Android - Authentication Screens

**Screens to Build**
1. **SplashActivity** - App launcher, check login state
2. **LoginActivity** - Email/password login with validation
3. **SignupActivity** - User registration form

**Features**
- Input validation (email format, password strength)
- Error handling and user feedback
- Loading states during API calls
- Store JWT token in SharedPreferences
- Navigate to HomeActivity on success

#### 2.5 Android - Theme Toggle

**Implementation**
- Create `themes.xml` (white theme) and `themes_dark.xml` (black theme)
- Store theme preference in SharedPreferences
- Toggle button in HomeActivity toolbar
- Apply theme change dynamically using `setTheme()`

#### 2.6 Testing Checklist
- [ ] Backend product APIs returning correct JSON
- [ ] Test category listing
- [ ] Test product search functionality
- [ ] Android app connects to backend successfully
- [ ] Login screen validates input correctly
- [ ] Signup creates user and returns token
- [ ] Token stored in SharedPreferences
- [ ] Theme toggle switches between black/white
- [ ] Navigation flow: Splash → Login → Home

#### 2.7 Deliverables
- Product APIs fully functional
- Android authentication screens working
- Theme toggle implemented
- MVVM architecture in place

---

## PHASE 3: Home & Product Features (~5-6 credits)

### Objectives
- Build Home screen with categories and featured products
- Implement product listing with RecyclerView
- Create product details screen
- Add search functionality
- Integrate Glide for image loading

### Tasks

#### 3.1 Android - Home Screen

**Layout: activity_home.xml**
- Toolbar with app logo, search icon, theme toggle
- Horizontal RecyclerView for categories
- "Featured Products" section header
- Vertical RecyclerView for featured products grid (2 columns)
- Bottom Navigation Bar (Home, Categories, Cart, Profile)

**Components**
- `HomeActivity.kt` - Main home screen
- `HomeViewModel.kt` - Handle data and business logic
- `CategoryAdapter.kt` - Display categories horizontally
- `ProductAdapter.kt` - Display products in grid

**Features**
- Load categories and featured products on launch
- Click category to filter products
- Click product card to open details
- Search icon opens SearchActivity
- Display product: image, name, price, rating, "Add to Cart" button

#### 3.2 Android - Product Listing Screen

**Layout: activity_product_list.xml**
- Toolbar with category name and back button
- Filter chips (Sort by: Price, Rating, Popularity)
- RecyclerView with product grid (2 columns)
- Loading state and empty state handling

**Components**
- `ProductListActivity.kt`
- `ProductViewModel.kt`
- Reuse `ProductAdapter.kt`

**Features**
- Receive categoryId from HomeActivity
- Load products by category
- Filter and sort functionality
- Pagination for large lists
- Pull-to-refresh

#### 3.3 Android - Product Details Screen

**Layout: activity_product_detail.xml**
- Large product image (use Glide)
- Product name and brand
- Star rating display
- Price (with discount if applicable)
- Product description
- Quantity selector (- / number / +)
- "Add to Cart" button (full width)
- Related products section (horizontal RecyclerView)

**Components**
- `ProductDetailActivity.kt`
- `ProductDetailViewModel.kt`

**Features**
- Load product details by productId
- Update quantity (min: 1, max: 10)
- Add to cart with selected quantity
- Show success message on add
- Load related products from same category

#### 3.4 Android - Search Functionality

**Layout: activity_search.xml**
- Search bar with auto-focus
- Recent searches list (stored locally)
- Search results RecyclerView
- "No results" state

**Components**
- `SearchActivity.kt`
- `SearchViewModel.kt`
- Reuse `ProductAdapter.kt`

**Features**
- Real-time search with debounce (500ms)
- Store recent searches in SharedPreferences
- Clear recent searches option
- Navigate to product details on click

#### 3.5 Image Loading with Glide

**Implementation**
- Create utility function for Glide loading
- Placeholder image while loading
- Error image if load fails
- Circular crop for category images
- Rounded corners for product images

**Example**
```kotlin
Glide.with(context)
    .load(imageUrl)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error_image)
    .into(imageView)
```

#### 3.6 Testing Checklist
- [ ] Home screen loads categories and products
- [ ] Categories scroll horizontally
- [ ] Products display in 2-column grid
- [ ] Images load correctly with Glide
- [ ] Click category filters products
- [ ] Product details screen shows all information
- [ ] Quantity selector works correctly
- [ ] Search returns relevant results
- [ ] Recent searches persist
- [ ] All screens responsive and smooth

#### 3.7 Deliverables
- Home screen fully functional
- Product listing with filters
- Product details screen complete
- Search functionality working
- Glide image loading integrated

---

## PHASE 4: Cart & Checkout (~5-6 credits)

### Objectives
- Build cart management APIs
- Implement cart screen with add/remove/update
- Create checkout flow
- Build order placement functionality

### Tasks

#### 4.1 Backend - Cart APIs

**API Endpoints**
- `GET /api/cart` - Get user's cart items
- `POST /api/cart/add` - Add product to cart
- `PUT /api/cart/update/:cartItemId` - Update quantity
- `DELETE /api/cart/remove/:cartItemId` - Remove item from cart
- `DELETE /api/cart/clear` - Clear entire cart

**Request/Response Examples**

*Add to Cart:*
```json
POST /api/cart/add
{
  "productId": 5,
  "quantity": 2
}

Response:
{
  "success": true,
  "message": "Product added to cart",
  "cartItem": {
    "id": 12,
    "productId": 5,
    "quantity": 2,
    "productName": "Fresh Milk",
    "price": 50.00,
    "image": "milk.jpg"
  }
}
```

#### 4.2 Backend - Order APIs

**API Endpoints**
- `POST /api/orders/create` - Place new order
- `GET /api/orders` - Get user's order history
- `GET /api/orders/:orderId` - Get order details
- `GET /api/orders/:orderId/track` - Get order tracking status

**Create Order Request:**
```json
POST /api/orders/create
{
  "addressId": 3,
  "deliveryAddress": "123 Main St, City, State, 12345",
  "paymentMethod": "COD",
  "items": [
    {
      "productId": 5,
      "quantity": 2,
      "price": 50.00
    }
  ],
  "totalAmount": 100.00
}
```

#### 4.3 Backend - Address APIs

**API Endpoints**
- `GET /api/addresses` - Get user's saved addresses
- `POST /api/addresses` - Add new address
- `PUT /api/addresses/:addressId` - Update address
- `DELETE /api/addresses/:addressId` - Delete address
- `PUT /api/addresses/:addressId/default` - Set as default

#### 4.4 Android - Cart Screen

**Layout: activity_cart.xml**
- Toolbar with "My Cart" title
- RecyclerView with cart items
- Each item shows: image, name, price, quantity selector, remove button
- Bottom section:
    - Subtotal
    - Delivery fee
    - Total amount
    - "Proceed to Checkout" button

**Components**
- `CartActivity.kt`
- `CartViewModel.kt`
- `CartAdapter.kt` with swipe-to-delete

**Features**
- Load cart items from API
- Update quantity (triggers API call)
- Remove item with confirmation
- Calculate total dynamically
- Empty cart state with "Browse Products" button
- Navigate to checkout

#### 4.5 Android - Checkout Flow

**Screen 1: Address Selection (activity_address_selection.xml)**
- List of saved addresses (RecyclerView)
- Radio button to select address
- "Add New Address" button
- "Continue" button

**Screen 2: Add/Edit Address (activity_add_address.xml)**
- Form fields:
    - Full Name
    - Phone Number
    - Address Line 1
    - Address Line 2
    - City
    - State
    - PIN Code
    - Save as (Home/Work/Other)
- "Save Address" button

**Screen 3: Order Summary (activity_order_summary.xml)**
- Delivery address display
- Order items list (non-editable)
- Price breakdown
- Payment method: "Cash on Delivery" (selected by default)
- "Place Order" button

**Components**
- `AddressSelectionActivity.kt`
- `AddAddressActivity.kt`
- `OrderSummaryActivity.kt`
- `CheckoutViewModel.kt`

#### 4.6 Android - Order Placement

**Features**
- Validate address before placing order
- Create order API call
- Show loading during order creation
- On success:
    - Clear cart locally
    - Show success message
    - Navigate to Order Tracking screen
- On failure:
    - Show error message
    - Allow retry

#### 4.7 Testing Checklist
- [ ] Add products to cart from different screens
- [ ] Cart displays all items correctly
- [ ] Update quantity reflects in total price
- [ ] Remove item works correctly
- [ ] Cart persists across app restarts
- [ ] Address form validates all fields
- [ ] Save address to backend
- [ ] Select address for delivery
- [ ] Order summary shows correct information
- [ ] Place order successfully
- [ ] Order created in database
- [ ] Cart cleared after order

#### 4.8 Deliverables
- Cart management fully functional
- Checkout flow complete
- Order placement working
- Address management implemented

---

## PHASE 5: Order Tracking & Polish (~5-6 credits)

### Objectives
- Implement order tracking with status updates
- Build order history screen
- Final UI/UX polish
- Testing and bug fixes
- Complete documentation

### Tasks

#### 5.1 Backend - Order Status Management

**Order Statuses:**
1. `PLACED` - Order received
2. `CONFIRMED` - Order confirmed by store
3. `PACKED` - Items packed and ready
4. `OUT_FOR_DELIVERY` - On the way
5. `DELIVERED` - Successfully delivered
6. `CANCELLED` - Order cancelled

**API Endpoints**
- `PUT /api/orders/:orderId/status` - Update order status (admin)
- `GET /api/orders/:orderId/track` - Get current status and timeline

**Tracking Response:**
```json
{
  "orderId": "ORD123456",
  "currentStatus": "OUT_FOR_DELIVERY",
  "estimatedDelivery": "2025-08-15T18:30:00",
  "timeline": [
    {
      "status": "PLACED",
      "timestamp": "2025-08-15T14:00:00",
      "completed": true
    },
    {
      "status": "CONFIRMED",
      "timestamp": "2025-08-15T14:15:00",
      "completed": true
    },
    {
      "status": "PACKED",
      "timestamp": "2025-08-15T15:30:00",
      "completed": true
    },
    {
      "status": "OUT_FOR_DELIVERY",
      "timestamp": "2025-08-15T16:45:00",
      "completed": true
    },
    {
      "status": "DELIVERED",
      "timestamp": null,
      "completed": false
    }
  ]
}
```

#### 5.2 Android - Order Tracking Screen

**Layout: activity_order_tracking.xml**
- Order ID and placement time
- Estimated delivery time (prominent)
- Vertical timeline showing order status stages
- Each stage:
    - Icon (checkmark if completed, circle if pending)
    - Status name
    - Timestamp (if completed)
    - Connecting line to next stage
- Order items summary (collapsible)
- Delivery address
- Total amount paid
- "Need Help?" button (contact support)
- "Cancel Order" button (if status allows)

**Components**
- `OrderTrackingActivity.kt`
- `OrderViewModel.kt`
- Custom `OrderTimelineView.kt` for status display

**Features**
- Load order details by orderId
- Display timeline with visual indicators
- Different colors for completed/pending stages
- Auto-refresh every 30 seconds (polling)
- Push notification on status change (optional)
- Cancel order with confirmation dialog

#### 5.3 Android - Order History Screen

**Layout: activity_order_history.xml**
- Toolbar with "My Orders" title
- Tabs: "Active" and "Past"
- RecyclerView with order cards
- Each card shows:
    - Order ID
    - Order date
    - Current status badge
    - Total items and amount
    - "Track Order" button

**Components**
- `OrderHistoryActivity.kt`
- `OrderHistoryViewModel.kt`
- `OrderAdapter.kt`

**Features**
- Load all user orders
- Filter by active/completed
- Click order to view tracking
- Reorder functionality (add same items to cart)
- Pull-to-refresh
- Pagination for large order lists

#### 5.4 Android - Profile Screen

**Layout: activity_profile.xml**
- User profile section:
    - Avatar placeholder
    - Name and email
    - "Edit Profile" button
- Menu items:
    - My Orders
    - Saved Addresses
    - Theme Toggle
    - About App
    - Logout

**Components**
- `ProfileActivity.kt`
- `ProfileViewModel.kt`

#### 5.5 Bottom Navigation Integration

**Tabs:**
1. Home - HomeActivity
2. Categories - CategoryListActivity
3. Cart - CartActivity (with badge showing item count)
4. Profile - ProfileActivity

**Implementation**
- Create `activity_main_container.xml` with BottomNavigationView
- Use Fragment-based navigation OR Activity switching
- Highlight current tab
- Cart badge updates on cart changes

#### 5.6 UI/UX Polish

**Design Improvements**
- Consistent color scheme (primary, secondary, accent)
- Typography: clear hierarchy with different text sizes
- Proper spacing and margins (16dp, 24dp guidelines)
- Loading states: shimmer effect for product lists
- Error states: friendly messages with retry buttons
- Empty states: illustrations with helpful text
- Animations:
    - Fade transitions between screens
    - Ripple effects on buttons
    - Add-to-cart animation
    - Cart badge pulse on update

**Accessibility**
- Content descriptions for images
- Sufficient touch target sizes (48dp minimum)
- High contrast in both themes
- Text scaling support

#### 5.7 Error Handling & Edge Cases

**Network Errors**
- No internet connection: show offline banner
- Timeout: show retry option
- Server error (500): friendly error message

**Empty States**
- Empty cart: "Your cart is empty" with "Browse Products" button
- No orders: "No orders yet" with "Start Shopping"
- Search no results: "No products found" with suggestions
- No saved addresses: "Add your first address"

**Validation**
- Email format validation
- Password strength indicator
- Phone number format (10 digits)
- PIN code validation
- Quantity limits (1-10 per product)

#### 5.8 Testing & Quality Assurance

**Functional Testing**
- [ ] Complete user journey: Signup → Browse → Add to cart → Checkout → Track order
- [ ] All CRUD operations working
- [ ] Authentication flow (login, logout, token expiry)
- [ ] Cart persistence
- [ ] Order placement and tracking
- [ ] Search and filters
- [ ] Theme toggle across all screens

**Performance Testing**
- [ ] App launches in < 3 seconds
- [ ] Product lists scroll smoothly (60fps)
- [ ] Images load without blocking UI
- [ ] API calls timeout handled gracefully
- [ ] Memory leaks checked (no activity leaks)

**UI Testing**
- [ ] Test on different screen sizes (4", 5", 6"+ screens)
- [ ] Test both themes (black and white)
- [ ] Portrait and landscape orientations
- [ ] Touch targets accessible
- [ ] Text readable at different zoom levels

#### 5.9 Documentation

**README.md files to create:**

1. **Root README.md**
    - Project overview
    - Technology stack
    - Features list
    - Setup instructions
    - Screenshots

2. **backend/README.md**
    - Prerequisites (Node.js version, MySQL)
    - Installation steps
    - Environment variables
    - API documentation
    - Database setup
    - Running the server

3. **Android README.md**
    - Prerequisites (Android Studio version, SDK)
    - Installation steps
    - Configuration (update BASE_URL)
    - Building the APK
    - Running on emulator/device

**API Documentation**
- Create API_DOCS.md with all endpoints
- Request/response examples
- Authentication requirements
- Error codes and messages

#### 5.10 Final Deliverables

**Backend**
- Complete Node.js + Express backend
- MySQL database with sample data
- All REST APIs functional
- Postman collection for testing
- Environment setup guide

**Android App**
- Complete APK ready to install
- All features implemented and tested
- XML layouts for all screens
- MVVM architecture properly implemented
- Retrofit + Glide integrated
- Black/White theme toggle

**Documentation**
- Complete setup instructions
- API documentation
- User manual (how to use the app)
- Developer guide (code structure)
- Troubleshooting guide

**Testing Results**
- Test report with all scenarios covered
- Known issues (if any)
- Performance metrics

---

## Project Setup Instructions

### Backend Setup

1. **Install Node.js** (v18 or higher)
   ```bash
   node --version
   npm --version
   ```

2. **Install MySQL 8.0**
   ```bash
   mysql --version
   ```

3. **Create Database**
   ```bash
   mysql -u root -p
   CREATE DATABASE blinkit_db;
   USE blinkit_db;
   SOURCE /path/to/database_schema.sql
   ```

4. **Setup Backend**
   ```bash
   cd backend
   npm install
   ```

5. **Configure Environment Variables**
   Create `.env` file:
   ```
   PORT=5000
   DB_HOST=localhost
   DB_USER=root
   DB_PASSWORD=yourpassword
   DB_NAME=blinkit_db
   JWT_SECRET=your_jwt_secret_key_here
   ```

6. **Run Server**
   ```bash
   npm start
   ```
   Server should run on http://localhost:5000

### Android Setup

1. **Install Android Studio** (latest version)
    - Download from https://developer.android.com/studio

2. **Open Project**
    - Open Android Studio
    - File → Open → Select project folder
    - Wait for Gradle sync

3. **Configure Backend URL**
    - Open `app/src/main/java/com/example/blinkit/utils/Constants.kt`
    - Update `BASE_URL` to your backend URL:
      ```kotlin
      const val BASE_URL = "http://10.0.2.2:5000/api/"  // For emulator
      // OR
      const val BASE_URL = "http://YOUR_IP:5000/api/"  // For physical device
      ```

4. **Run App**
    - Connect Android device OR start emulator
    - Click "Run" button (green play icon)
    - Select device
    - Wait for build and installation

---

## Cost Estimation

**Phase 1:** ~5-6 credits (Database + Auth Backend)
**Phase 2:** ~5-6 credits (Products Backend + Android Foundation)
**Phase 3:** ~5-6 credits (Home & Product Features)
**Phase 4:** ~5-6 credits (Cart & Checkout)
**Phase 5:** ~5-6 credits (Order Tracking & Polish)

**Total Estimated Cost:** ~25-30 credits

---

## Notes

- Each phase builds upon the previous one
- Testing is done incrementally at the end of each phase
- Sample data should be inserted for testing purposes
- Backend must be running for Android app to work
- Use Postman to test APIs before integrating with Android
- Follow MVVM pattern strictly for clean architecture
- Keep code modular and reusable
- Comment complex logic for maintainability

---

**End of Workflows Document**