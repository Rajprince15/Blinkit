

# Blinkit Clone - Grocery Delivery App

A full-featured grocery delivery Android application inspired by Blinkit, built with modern Android development practices and a robust Node.js backend.

## 🚀 Features

### ✅ Phase 1-4 Complete
- **User Authentication** - Signup, Login, JWT-based authentication
- **Product Catalog** - Browse categories, featured products, product details
- **Search Functionality** - Real-time search with recent searches
- **Shopping Cart** - Add, update, remove items with quantity management
- **Checkout Flow** - Address management, order summary, order placement
- **Order Management** - Order history, order tracking with status timeline

### ✅ Phase 5 Complete
- **Order Tracking** - Visual timeline showing order status progression
- **Order History** - View active and past orders
- **User Profile** - Profile management with settings
- **Theme Toggle** - Switch between light and dark themes
- **Bottom Navigation** - Easy navigation between main sections
- **Admin Features** - Order status management (backend)

## 🛠️ Technology Stack

### Mobile (Android)
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **UI:** XML Layouts with Material Design components
- **Networking:** Retrofit 2 with OkHttp
- **Image Loading:** Glide
- **Async Operations:** Kotlin Coroutines
- **Authentication:** JWT Token with Auto-injection via Interceptor

### Backend
- **Runtime:** Node.js
- **Framework:** Express.js
- **Database:** MySQL 8.0
- **Authentication:** JWT (JSON Web Tokens)
- **Password Security:** bcrypt

## 📱 App Screenshots

_(Add screenshots here after testing)_

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Node.js v18+
- MySQL 8.0
- Git

### Backend Setup

See [Backend README](Backend/README.md) for detailed setup instructions.

Quick start:
```bash
cd Backend
npm install
# Configure .env file (see Backend README)
npm start
```

### Android Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Rajprince15/Blinkit.git
   cd Blinkit
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project folder and select it
   - Wait for Gradle sync to complete

3. **Configure Backend URL**
   - Open `app/src/main/java/com/example/blinkit/utils/Constants.kt`
   - Update `BASE_URL`:
     ```kotlin
     const val BASE_URL = "http://10.0.2.2:5000/api/"  // For Android Emulator
     // OR
     const val BASE_URL = "http://YOUR_IP_ADDRESS:5000/api/"  // For Physical Device
     ```

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon)
   - Select your device
   - Wait for the build to complete

## 📚 Project Structure

```
Blinkit/
├── app/                          # Android application
│   └── src/main/
│       ├── java/com/example/blinkit/
│       │   ├── activities/       # UI Activities
│       │   ├── adapters/         # RecyclerView Adapters
│       │   ├── api/              # Retrofit API clients
│       │   ├── models/           # Data models
│       │   ├── repositories/     # Data repositories
│       │   ├── viewmodels/       # ViewModels
│       │   └── utils/            # Utility classes
│       └── res/                  # Resources (layouts, drawables, etc.)
├── Backend/                      # Node.js backend
│   ├── controllers/              # Request handlers
│   ├── routes/                   # API routes
│   ├── middleware/               # Auth & other middleware
│   ├── models/                   # Database models
│   └── config/                   # Configuration files
└── database.sql                  # MySQL schema
```

## 🔑 Key Features Implementation

### Auto Token Injection
The app uses an **auth interceptor** in `ApiClient` that automatically:
- Retrieves JWT token from SharedPreferences
- Injects it into all API requests
- No manual token passing required in repositories

### MVVM Architecture
- **Model:** Data classes and repositories
- **View:** Activities and XML layouts
- **ViewModel:** Business logic and state management

### Order Tracking
- Real-time status updates
- Visual timeline with 5 stages:
  1. PLACED
  2. CONFIRMED
  3. PACKED
  4. OUT_FOR_DELIVERY
  5. DELIVERED

## 🧪 Testing

### Backend Testing
Use Postman or curl to test APIs:
```bash
# Test signup
curl -X POST http://localhost:5000/api/auth/signup 
  -H "Content-Type: application/json" 
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'
```

### Android Testing
1. Run the app on emulator/device
2. Test complete user flow:
   - Signup → Login → Browse → Add to Cart → Checkout → Track Order
3. Test theme switching
4. Test search functionality
5. Verify order tracking timeline

## 📝 API Documentation

See [Backend README](Backend/README.md) for complete API documentation.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Rajprince15**
- GitHub: [@Rajprince15](https://github.com/Rajprince15)

## 🙏 Acknowledgments

- Inspired by Blinkit (formerly Grofers)
- Built with love for learning Android development
- Thanks to the open-source community

## 📞 Support

For issues and questions:
- Open an issue on GitHub
- Email: (your-email@example.com)

---

**Made with ❤️ for learning and portfolio purposes**
