plugins {
alias(libs.plugins.android.application)
alias(libs.plugins.kotlin.android)
id("org.jetbrains.kotlin.kapt")
}

android {
namespace = "com.example.blinkit"
compileSdk = 34


defaultConfig {
applicationId = "com.example.blinkit"
minSdk = 24
targetSdk = 34
versionCode = 1
versionName = "1.0"

testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
vectorDrawables {
useSupportLibrary = true
}
}

buildTypes {
release {
isMinifyEnabled = false
proguardFiles(
getDefaultProguardFile("proguard-android-optimize.txt"),
"proguard-rules.pro"
)
}
}
compileOptions {
sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17
}

kotlinOptions {
jvmTarget = "17"
}
buildFeatures {
viewBinding = true
compose = true
}
composeOptions {
kotlinCompilerExtensionVersion = "1.5.8"
}
packaging {
resources {
excludes += "/META-INF/{AL2.0,LGPL2.1}"
}
}
}

dependencies {
// Core Android
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.appcompat)
implementation(libs.material)
implementation(libs.androidx.constraintlayout)

// Lifecycle
implementation(libs.androidx.lifecycle.runtime.ktx)
implementation(libs.androidx.lifecycle.viewmodel.ktx)
implementation(libs.androidx.lifecycle.livedata.ktx)

// Activity
implementation(libs.androidx.activity.ktx)

// ✅ FIX #1: Added Activity Compose dependency for setContent
implementation("androidx.activity:activity-compose:1.9.1")

// RecyclerView
implementation(libs.androidx.recyclerview)

// SwipeRefreshLayout
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

// Retrofit for networking
implementation(libs.retrofit)
implementation(libs.retrofit.converter.gson)
implementation(libs.okhttp.logging)

// Glide for image loading
implementation(libs.glide)
kapt(libs.glide.compiler)

// Coroutines
implementation(libs.kotlinx.coroutines.android)
implementation(libs.kotlinx.coroutines.core)

// Compose
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.ui)
implementation(libs.androidx.ui.graphics)
implementation(libs.androidx.ui.tooling.preview)
implementation(libs.androidx.material3)

// Testing
testImplementation(libs.junit)
androidTestImplementation(libs.androidx.junit)
androidTestImplementation(libs.androidx.espresso.core)
androidTestImplementation(platform(libs.androidx.compose.bom))
androidTestImplementation(libs.androidx.ui.test.junit4)
debugImplementation(libs.androidx.ui.tooling)
debugImplementation(libs.androidx.ui.test.manifest)

}
