# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# --- General Android Rules ---
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# --- Firebase Rules ---
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# --- Hilt / Dagger Rules ---
-keep class dagger.hilt.android.internal.** { *; }
-keep class * extends javax.inject.Provider
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keep class * extends dagger.hilt.internal.GeneratedComponentManager
-keep class * extends dagger.hilt.internal.UnsafeCasts

# --- Jetpack Compose Rules ---
-keep class androidx.compose.runtime.Recomposer { *; }
-keep class androidx.compose.ui.platform.** { *; }
-keep class androidx.compose.foundation.layout.** { *; }
-dontwarn androidx.compose.**

# --- Kotlin Serialization Rules ---
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}
-keepclassmembers class ** {
    @kotlinx.serialization.SerialName *;
}
-keep class kotlinx.serialization.json.** { *; }

# --- Coil Rules ---
-keep class coil.** { *; }
-dontwarn coil.**

# --- UI State & Data Models ---
# Keep your data classes to prevent issues with serialization/reflection
-keep class com.example.scrapuncle.auth.uistate.** { *; }
-keep class com.example.scrapuncle.auth.data.** { *; }

# --- Misc ---
-dontwarn okio.**
-dontwarn javax.annotation.**