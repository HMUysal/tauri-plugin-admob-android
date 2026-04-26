
# Tauri Plugin admob-android


`File`:`src-tauri/gen/android/build.gradle.kts`
```kotlin
// Upgrade to 8.11.1 (latest bugfix for your branch)
classpath("com.android.tools.build:gradle:8.11.1") 

// UPGRADE THIS: AdMob 25.x requires Kotlin 2.1.0 or higher
classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")

```



`File`:`src-tauri/gen/android/app/build.gradle.kts`
```kotlin
//
implementation("com.google.android.gms:play-services-ads:25.2.0")
```
`File`:`src-tauri/gen/android/app/build.gradle.kts`
```kotlin
defaultConfig {
        multiDexEnabled = true
}
```


`File`:`src-tauri/gen/android/app/src/main/AndroidManifest.xml`
```xml
<!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="SAMPLE_APP_ID"/>

<!-- For apps targeting Android 13 or higher & GMA SDK version 20.3.0 or lower -->
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

```
