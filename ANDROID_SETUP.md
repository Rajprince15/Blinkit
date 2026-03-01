# Android App Setup Guide

Complete guide to set up and run the Blinkit Clone Android application.

## 📋 Prerequisites

- **Android Studio:** Arctic Fox (2020.3.1) or later
- **JDK:** Version 11 or higher
- **Android SDK:** API Level 24 (Android 7.0) minimum, API Level 34 (Android 14) target
- **Gradle:** 8.0+ (handled by Android Studio)
- **Backend:** Node.js backend running (see [Backend README](../Backend/README.md))

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/Rajprince15/Blinkit.git
cd Blinkit
```

### 2. Open in Android Studio
1. Launch Android Studio
2. Select **"Open an existing project"**
3. Navigate to the cloned `Blinkit` folder
4. Click **"OK"**
5. Wait for Gradle sync to complete (may take a few minutes)

### 3. Configure Backend URL

**For Android Emulator:**
```kotlin
// app/src/main/java/com/example/blinkit/utils/Constants.kt
const val BASE_URL = "http://10.0.2.2:5000/api/"
```
*Note: `10.0.2.2` is the special IP that emulator uses to access host machine's localhost*

**For Physical Device:**
```kotlin
// app/src/main/java/com/example/blinkit/utils/Constants.kt
const val BASE_URL = "http://YOUR_COMPUTER_IP:5000/api/"
```

To find your IP address:
- **Windows:** `ipconfig` (look for IPv4 Address)
- **Mac/Linux:** `ifconfig` or `ip addr show`

### 4. Sync Project
- Click **"Sync Project with Gradle Files"** (🐘 icon in toolbar)
- Wait for sync to complete

### 5. Run the App
1. **Start an Emulator** OR **Connect Physical Device**
   - For emulator: Tools → Device Manager → Create/Start Device
   - For physical device: Enable USB Debugging in Developer Options

2. **Click Run** (▶️ green play button)
3. **Select Device** from the list
4. Wait for build and installation

## 🏗️ Project Architecture

### MVVM Pattern
```
app/
├── activities/          # UI Layer (Views)
├── viewmodels/          # Business Logic Layer
├── repositories/        # Data Layer
├── models/              # Data Models
├── api/                 # Network Layer
├── adapters/            # RecyclerView Adapters
└── utils/               # Utilities & Helpers
```

### Key Components

#### 1. ApiClient (Auto Token Injection)
- Located: `app/api/ApiClient.kt`
- **Features:**
  - Singleton Retrofit instance
  - Auth interceptor automatically injects JWT token
  - Logging interceptor for debugging
  - 30-second timeout configuration

#### 2. Repositories
All repositories automatically use auth token via interceptor:
- `AuthRepository` - Login, Signup, Profile
- `ProductRepository` - Products, Categories, Search
- `CartRepository` - Cart management
- `OrderRepository` - Order operations
- `AddressRepository` - Address management

#### 3. ViewModels
- `AuthViewModel` - Authentication state
- `ProductViewModel` - Product data
- `CartViewModel` - Cart state
- `OrderViewModel` - Order management

## 🎨 UI Components

### Activities
- **SplashActivity** - App entry point, checks login state
- **LoginActivity** - User authentication
- **SignupActivity** - New user registration
- **HomeActivity** - Main screen with categories & products
- **ProductListActivity** - Category products
- **ProductDetailActivity** - Product details
- **SearchActivity** - Product search
- **CartActivity** - Shopping cart
- **OrderTrackingActivity** - Order status timeline
- **OrderHistoryActivity** - Past orders
- **ProfileActivity** - User profile & settings
- **MainContainerActivity** - Bottom navigation container

### Layouts
All layouts use Material Design components and follow XML best practices.

## 🔧 Configuration Files

### build.gradle.kts (Module: app)
Key dependencies:
```kotlin
// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Image Loading
implementation("com.github.bumptech.glide:glide:4.16.0")

// ViewModel & LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Material Design
implementation("com.google.android.material:material:1.11.0")
```

### AndroidManifest.xml
- Permissions: INTERNET, ACCESS_NETWORK_STATE
- usesCleartextTraffic: true (for HTTP connections)
- Application class: BlinkitApplication

## 🎯 Features Implementation

### 1. Authentication Flow
```
SplashActivity → Check Token → LoginActivity/HomeActivity
```

### 2. Auto Token Management
- Token stored in SharedPreferences
- ApiClient interceptor automatically injects token
- No manual token passing required

### 3. Theme Toggle
- Light/Dark theme support
- Stored in SharedPreferences
- Applied via ThemeManager

### 4. Order Tracking
- 5-stage visual timeline
- Auto-refresh every 30 seconds
- Real-time status updates

## 🧪 Testing

### Manual Testing Checklist
- [ ] App launches without crashes
- [ ] Signup creates new user
- [ ] Login authenticates user
- [ ] Token stored and persists
- [ ] Products load on home screen
- [ ] Search returns results
- [ ] Add to cart works
- [ ] Cart updates quantities
- [ ] Checkout flow completes
- [ ] Order placed successfully
- [ ] Order tracking shows timeline
- [ ] Theme toggle works
- [ ] Logout clears session

### Testing with Emulator
1. Use Android Emulator (Pixel 5, API 34 recommended)
2. Ensure backend is running on `http://localhost:5000`
3. Use `10.0.2.2:5000` as BASE_URL

### Testing with Physical Device
1. Connect device via USB
2. Enable Developer Options & USB Debugging
3. Ensure device and computer on same network
4. Use computer's IP address in BASE_URL
5. Backend must be accessible from device

## 🐛 Common Issues & Solutions

### Issue: "Unable to resolve dependency"
**Solution:**
```bash
# Invalidate caches and restart
File → Invalidate Caches → Invalidate and Restart
```

### Issue: "Could not connect to backend"
**Solution:**
- Check BASE_URL configuration
- Ensure backend is running
- For emulator: Use `10.0.2.2`
- For device: Use computer's IP and ensure same network
- Check firewall settings

### Issue: "Failed to install APK"
**Solution:**
- Uninstall old version from device
- Clean project: Build → Clean Project
- Rebuild: Build → Rebuild Project

### Issue: Glide images not loading
**Solution:**
- Check INTERNET permission in manifest
- Verify image URLs are valid
- Check backend is serving images correctly

### Issue: App crashes on startup
**Solution:**
- Check logcat for error messages
- Verify all required permissions
- Ensure Application class is registered in manifest
- Check if all dependencies are synced

## 📱 Building APK

### Debug APK
```bash
# Via Android Studio
Build → Build Bundle(s) / APK(s) → Build APK(s)

# Via command line
./gradlew assembleDebug
```
APK location: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK
1. Create keystore (one-time):
```bash
keytool -genkey -v -keystore blinkit-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias blinkit
```

2. Configure `build.gradle.kts`:
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("blinkit-release.jks")
            storePassword = "your_password"
            keyAlias = "blinkit"
            keyPassword = "your_password"
        }
    }
}
```

3. Build release:
```bash
./gradlew assembleRelease
```

## 📊 Performance Tips

### Optimize Images
- Use appropriate image sizes
- Enable Glide caching
- Use placeholder images

### Network Optimization
- Implement pagination for large lists
- Cache API responses
- Use appropriate timeout values

### Memory Management
- Clear image cache when needed
- Use ViewBinding to prevent memory leaks
- Implement proper lifecycle management

## 🔐 Security Best Practices

- Never commit `.jks` files or passwords
- Use ProGuard for release builds
- Implement certificate pinning for production
- Store sensitive data securely (use EncryptedSharedPreferences in production)
- Validate all user inputs

## 📚 Useful Commands

```bash
# Check Gradle version
./gradlew --version

# List all tasks
./gradlew tasks

# Run lint checks
./gradlew lint

# Run tests
./gradlew test

# Clean build
./gradlew clean

# Install APK to connected device
./gradlew installDebug
```

## 🆘 Getting Help

### Resources
- [Android Developer Guide](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Glide Documentation](https://bumptech.github.io/glide/)

### Debugging
1. Enable detailed logging in LogCat
2. Use Android Profiler for performance issues
3. Check backend logs for API errors
4. Use Postman to test backend APIs independently

---

## 🎓 Learning Resources

- MVVM Architecture: [Google's Guide](https://developer.android.com/topic/architecture)
- Kotlin Coroutines: [Official Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- Material Design: [Material.io](https://material.io/develop/android)

---

**Happy Coding! 🚀**
