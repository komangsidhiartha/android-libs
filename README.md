# android-libs: An Internal Android SDK for Rapid App Development

---

## Overview

This repository contains `android-libs`, an internal SDK I architected and developed to standardize core functionalities and accelerate the development of native Android applications at PT G.I.T. By providing a suite of pre-built, reusable components for features like analytics, networking, and UI, this library became the architectural foundation for major apps like **Mamikos** and **Jakpat**, ensuring consistency and dramatically reducing boilerplate code.

---

## The Problem: Scaling Mobile Development

As we developed and maintained multiple applications simultaneously (Mamikos, Jakpat), we faced significant challenges that hampered our velocity and scalability:
* **Code Duplication:** Core features like analytics, networking, and user notifications were being built from scratch for each project, leading to inconsistent implementations and wasted development time.
* **Inconsistent Architecture:** Without a shared foundation, each application had a slightly different architecture, making it difficult for developers to switch between projects.
* **Slow Onboarding:** New developers had a steep learning curve, as they needed to understand the unique quirks of each separate codebase.

---

## The Solution: A Centralized Core Library

To solve these challenges, I developed `android-libs` to provide a centralized, easy-to-use API for all common application needs.

### Key Features

#### 🏗️ Base Components
- **BaseActivity** - Foundation activity with toolbar, progress bar, and fragment management.
- **BaseDrawerActivity** - Activity with built-in navigation drawer support.
- **BaseSupportFragment** - Base fragment with lifecycle management.
- **BaseDialog** - Simplified dialog fragment implementation.
- **BaseApplication** - Application class with integrated Firebase Analytics.

#### 🌐 Networking
- **BaseAPI** - Abstract API client built on the Fuel HTTP library with automatic JSON serialization (GSON), configurable headers, built-in error handling, and multipart form data support.

#### 📱 UI Components
- **RecyclerAdapter** - Generic RecyclerView adapter with automatic pagination, load-more functionality, and support for multiple layout managers.

#### 🛠️ Utilities
- **GSONManager** - Centralized GSON configuration for consistent JSON handling.

#### 📊 Extensions
- **AnyExtension** - Debug logging extension that only logs in debug builds.
- **ButtonExtension** - Simple button click tracking with Firebase Analytics.

---

## Impact & Results

Adopting `android-libs` as the foundation for our new projects had a direct and positive impact on the team and business.

* **Accelerated Development:** Reduced the time to implement core features in new projects by an estimated **40-50%**.
* **Standardized Architecture:** Ensured a consistent technical implementation and user experience across both the Jakpat and Mamikos applications, making it easier for developers to contribute to either project.
* **Improved Maintainability:** Allowed our team to roll out updates and bug fixes to core features (like a new analytics event) in one central place, benefiting all projects simultaneously.

---

## How to Use (API Examples)

The primary goal of the library was to be powerful yet simple to use, requiring minimal code to implement complex features.

### BaseActivity
```kotlin
class MainActivity : BaseActivity() {
    override val layoutResource: Int = R.layout.activity_main
    
    override fun viewDidLoad() {
        // Initialize your views and setup logic here
        showLoadingBar()
        
        // Your code...
        
        hideLoadingBar()
    }
}
```

### BaseAPI
```kotlin
class UserAPI(private val userId: String) : BaseAPI() {
    override val headers: Map<String, String>? = mapOf("Authorization" to "Bearer YOUR_TOKEN")
    override val basePath: String = "https://api.example.com"
    override val method: APIMethod = APIMethod.GET
    override val path: String = "users/$userId"
    override val params: String = ""
    
    fun fetchUser(callback: (User?, String?) -> Unit) {
        execute(User::class.java, callback)
    }
}

// Usage
UserAPI("123").fetchUser { user, error ->
    if (error != null) {
        // Handle error
    } else {
        // Use user data
    }
}
```

### RecyclerAdapter
```kotlin
class MyAdapter(context: Context, items: MutableList<Item>) 
    : RecyclerAdapter<Item, MyAdapter.ViewHolder>(context, items) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }
    
    override fun loadMore() {
        // Load more items from API
        // ... fetch data ...
        addItems(newItems)
    }
    
    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(item: Item) {
            // Bind item data to views
        }
    }
}
```

### Extensions
```kotlin
// Debug logging
this.logIfDebug("User logged in successfully")

// Button with automatic analytics tracking
myButton.onTrackedClick("login_button_tapped") {
    // Button click action
}
```

---

## Installation & Technical Specifications
### Installation
Add this library module to your project's build.gradle file:
```gradle
dependencies {
    implementation project(':android-libs')
}
```

### Requirements
- Min SDK: 16
- Target SDK: 29
- Compile SDK: 29

### Dependencies
- AndroidX - AppCompat, RecyclerView, Material Design
- Kotlin - Standard library
- Fuel - HTTP networking library
- GSON - JSON serialization/deserialization
- Firebase - Analytics and Cloud Messaging
- Anko - Kotlin Android extensions

### Permissions
The library requires the following permissions, which are declared in its AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### ProGuard
The library is configured to work with ProGuard/R8 out of the box.

---

## Project Status
This was an internal library developed at PT G.I.T. and has since been archived. The code is provided for portfolio and demonstration purposes.
