 # tauri-plugin-admob-android
 
 A simple and efficient Tauri plugin to integrate **Google AdMob** into your Android applications. This plugin allows you to display Banner and Interstitial ads with ease.
 
 ## Features
 
 - **Easy Initialization:** Set up AdMob SDK with a single function call.
 - **Consent Management:** Built-in support for requesting user consent (GDPR/UMP).
 - **Multiple Ad Formats:** Support for Banners (Top/Bottom) and Interstitial ads.
 - **Tauri 2.0 Ready:** Designed specifically for the latest Tauri mobile ecosystem.
 
 ## Installation
 
 ### Using Tauri CLI (Recommended)
 
 The easiest way to install this plugin is using the Tauri CLI, which automatically adds both Rust and JavaScript dependencies to your project:
 
 ```bash
 # Using npm
 npm run tauri add admob-android
 
 # Using pnpm
 pnpm tauri add admob-android
 
 # Using yarn
 yarn tauri add admob-android
 
 # Using bun
 bun tauri add admob-android
 ```
 
 This will:
 - Add the `tauri-plugin-admob-android` crate to your `Cargo.toml`.
 - Install the `tauri-plugin-admob-android-api` npm package.
 - Set up the necessary configurations and permissions.
 
 ---
 
 ### Manual Installation
 
 If you prefer to manually install the plugin, follow these steps:
 
 #### 1. Rust Dependencies
 
 Add the plugin to your `src-tauri/Cargo.toml` file:
 
 ```toml
[dependencies]
tauri-plugin-admob-android = "0.1.8"
 ```
 
 #### 2. JavaScript/TypeScript API
 
 Install the frontend package:
 
 ```bash
 yarn add tauri-plugin-admob-android-api
 # or
 npm install tauri-plugin-admob-android-api
 ```
 
 ---
 
 ## Setup
 
 ### Android Build Configuration
 Update your Kotlin and Gradle versions in `src-tauri/gen/android/build.gradle.kts` to ensure compatibility:
 
 ```kotlin
 dependencies {
     classpath("com.android.tools.build:gradle:8.2.1")
     classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
 }
 ```
 
 ### Android Manifest
 Add your **AdMob App ID** and required permissions to `src-tauri/gen/android/app/src/main/AndroidManifest.xml`. Replace `YOUR_ADMOB_APP_ID` with your actual ID from the AdMob dashboard. Or you can test it with Test ID `ca-app-pub-3940256099942544~3347511713`
 
 ```xml
 <manifest>
     <application>
        <meta-data
             android:name="com.google.android.gms.ads.APPLICATION_ID"
             android:value="YOUR_ADMOB_APP_ID"/>
     </application>
 
          <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     <uses-permission android:name="android.permission.INTERNET" />
 </manifest>
 ```
 
 ## Usage
 
 ```javascript
 import {} from 'tauri-plugin-admob-android-api';
 
  // 1. Initialize AdMob SDK
 await initialize();
 
 // 2. Request Consent (GDPR/UMP)
 await requestConsent();
 
 // 3 Show Ad
 //     3.a Display a Banner Ad
 await loadBanner({
   position: 'bottom',
   adUnitId: 'ca-app-pub-3940256099942544/6300978111' 
 });
 //     3.b Display a Banner Ad
 await loadInterstitial({
   adUnitId: 'ca-app-pub-3940256099942544/6300978111' 
 });
 ```
 
 ## License
 MIT