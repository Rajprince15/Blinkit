Great question! Let me provide you with a complete step-by-step guide to run your Blinkit Android app through Android Studio.

## 📱 COMPLETE GUIDE TO RUN YOUR ANDROID APP

---

## 🔧 STEP 1: Setup Backend (Must be running first)

### **1.1 Start MySQL Database**
```bash
# Start MySQL service
# Windows:
net start MySQL80

# Mac:
mysql.server start

# Linux:
sudo systemctl start mysql
```

### **1.2 Create Database and Load Schema**
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE blinkit_db;

# Exit MySQL
exit

# Load schema from your project
cd /app/Blinkit
mysql -u root -p blinkit_db < database.sql
```

### **1.3 Configure Backend .env File**
```bash
cd /app/Blinkit/Backend

# Create .env file if not exists
nano .env
```

Add this content:
```env
PORT=5000
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=your_mysql_password
DB_NAME=blinkit_db
JWT_SECRET=your_jwt_secret_key_here_make_it_long_and_random
JWT_EXPIRES_IN=7d
```

### **1.4 Install Dependencies and Start Backend**
```bash
# Install Node.js dependencies
npm install

# Start the backend server
npm start

# You should see: "Server running on port 5000"
```

**Keep this terminal window open - backend must keep running!**

---

## 📱 STEP 2: Setup Android Studio

### **2.1 Install Android Studio**
1. Download from: https://developer.android.com/studio
2. Install with default settings
3. During first run, it will download required SDKs

### **2.2 Open Your Project**
1. Launch Android Studio
2. Click **"Open"** or **"Open an Existing Project"**
3. Navigate to: `/app/Blinkit`
4. Click **"OK"**

### **2.3 Wait for Gradle Sync**
- Android Studio will automatically start syncing
- This may take 5-10 minutes first time
- You'll see progress at the bottom: "Gradle sync in progress..."
- Wait until it says: "Gradle sync finished"

---

## 🌐 STEP 3: Configure Backend URL

### **3.1 Find Your IP Address**

**For Physical Device (Recommended):**
```bash
# Windows:
ipconfig
# Look for "IPv4 Address" (e.g., 192.168.1.100)

# Mac/Linux:
ifconfig
# Look for "inet" under your network adapter (e.g., 192.168.1.100)
```

### **3.2 Update Constants.kt**
1. In Android Studio, navigate to:
   ```
   app/src/main/java/com/example/blinkit/utils/Constants.kt
   ```

2. Update the BASE_URL:
   ```kotlin
   object Constants {
       // For Android Emulator:
       const val BASE_URL = "http://10.0.2.2:5000/api/"
       
       // OR for Physical Device (replace with YOUR IP):
       // const val BASE_URL = "http://192.168.1.100:5000/api/"
   }
   ```

3. Press **Ctrl+S** (Windows/Linux) or **Cmd+S** (Mac) to save

---

## 🚀 STEP 4: Run the App

### **Option A: Using Android Emulator (Easier)**

#### **4.1 Create Emulator**
1. Click **Device Manager** icon (phone icon in toolbar)
2. Click **"Create Device"**
3. Select **"Pixel 5"** or any phone
4. Click **"Next"**
5. Select **"Tiramisu"** (API 33) or **"UpsideDownCake"** (API 34)
6. Click **"Download"** if needed, then **"Next"**
7. Name it (e.g., "Pixel_5_API_33")
8. Click **"Finish"**

#### **4.2 Start Emulator**
1. In Device Manager, click **▶ Play** button next to your emulator
2. Wait for emulator to boot (1-2 minutes)

#### **4.3 Run App**
1. Make sure emulator is shown in device dropdown (top toolbar)
2. Click green **▶ Run** button (or press **Shift+F10**)
3. Wait for app to build and install (2-3 minutes first time)
4. App will launch automatically!

---

### **Option B: Using Physical Android Device**

#### **4.1 Enable Developer Mode on Phone**
1. Go to **Settings** → **About Phone**
2. Tap **"Build Number"** 7 times rapidly
3. You'll see: "You are now a developer!"

#### **4.2 Enable USB Debugging**
1. Go to **Settings** → **System** → **Developer Options**
2. Turn on **"USB Debugging"**
3. Turn on **"Install via USB"**

#### **4.3 Connect Phone to Computer**
1. Connect phone via USB cable
2. On phone, tap **"Allow"** when prompted for USB debugging
3. Check "Always allow from this computer"

#### **4.4 Verify Connection**
```bash
# In Android Studio terminal:
adb devices

# You should see your device listed like:
# List of devices attached
# ABC123DEF456    device
```

#### **4.5 Run App**
1. Select your device from dropdown (top toolbar)
2. Click green **▶ Run** button
3. App will install and launch on your phone!

---

## 🔍 STEP 5: Test the App

### **5.1 First Launch - Signup**
1. App opens to Splash Screen
2. Click **"Sign Up"**
3. Enter:
   - Name: Test User
   - Email: test@example.com
   - Password: test123
   - Phone: 1234567890
4. Click **"Sign Up"**
5. You should be logged in!

### **5.2 Test Features**
- ✅ Browse products on Home screen
- ✅ Search for products
- ✅ Add items to cart
- ✅ View cart
- ✅ Add delivery address
- ✅ View profile

---

## ⚠️ TROUBLESHOOTING

### **Issue 1: "Unable to connect to backend"**
**Solution:**
```bash
# Check backend is running
curl http://localhost:5000/api/categories

# If using physical device, check firewall:
# Windows: Allow port 5000 in Windows Firewall
# Mac: System Preferences → Security → Firewall → Allow
```

### **Issue 2: "Gradle sync failed"**
**Solution:**
1. Click **File** → **Invalidate Caches**
2. Select **"Invalidate and Restart"**
3. Wait for Android Studio to restart

### **Issue 3: "SDK not found"**
**Solution:**
1. Click **Tools** → **SDK Manager**
2. Install **Android SDK 33** or higher
3. Click **"Apply"** and wait for download

### **Issue 4: App crashes on launch**
**Solution:**
```bash
# Check logs in Android Studio:
# View → Tool Windows → Logcat
# Look for error messages in red
```

### **Issue 5: Emulator is slow**
**Solution:**
1. Close other applications
2. Allocate more RAM to emulator:
   - Device Manager → Click ⋮ → Edit
   - Advanced Settings → RAM: 2048 MB or higher

---

## 📊 VERIFY EVERYTHING IS WORKING

### **Backend Checklist:**
```bash
# Test backend APIs
curl http://localhost:5000/api/categories
# Should return JSON with success: true

# Check backend logs
# You should see API requests being logged
```

### **Android App Checklist:**
- ✅ App launches without crash
- ✅ Signup creates new user
- ✅ Login works with credentials
- ✅ Products load on home screen
- ✅ Images load correctly
- ✅ Cart operations work
- ✅ Theme toggle works

---

## 🎯 QUICK START COMMANDS

### **Terminal 1 (Backend):**
```bash
cd /app/Blinkit/Backend
npm start
```

### **Terminal 2 (Check Backend):**
```bash
curl http://localhost:5000/api/categories
```

### **Android Studio:**
1. Open project: `/app/Blinkit`
2. Wait for Gradle sync
3. Click ▶ Run button

---

