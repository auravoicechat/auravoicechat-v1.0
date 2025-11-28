# ProGuard Rules for Aura Voice Chat
# Developer: Hawkaye Visions LTD — Pakistan

# ========================
# General Rules
# ========================

# Keep source file names and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Keep annotations
-keepattributes *Annotation*

# Keep generic signatures
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# ========================
# Application Models
# ========================

# Keep model classes for JSON parsing
-keep class com.aura.voicechat.data.model.** { *; }
-keep class com.aura.voicechat.domain.model.** { *; }

# Keep data classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ========================
# Firebase
# ========================

-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase Auth
-keep class com.google.firebase.auth.** { *; }

# Firebase Crashlytics
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }

# Firebase Firestore
-keep class com.google.firebase.firestore.** { *; }

# ========================
# Retrofit
# ========================

-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

# ========================
# OkHttp
# ========================

-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# ========================
# Gson
# ========================

-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from removing fields that Gson uses
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ========================
# Coroutines
# ========================

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ========================
# Kotlin
# ========================

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keep class kotlin.reflect.jvm.internal.** { *; }

# ========================
# Jetpack Compose
# ========================

-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
-keep class androidx.lifecycle.** { *; }

# ========================
# Hilt / Dagger
# ========================

-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager.FragmentContextWrapper { *; }

# ========================
# WebRTC
# ========================

-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**

# ========================
# ExoPlayer / Media3
# ========================

-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# ========================
# ML Kit
# ========================

-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# ========================
# CameraX
# ========================

-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# ========================
# Room Database
# ========================

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ========================
# Lottie
# ========================

-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# ========================
# Coil
# ========================

-keep class io.coil.** { *; }
-dontwarn io.coil.**

# ========================
# Facebook SDK
# ========================

-keep class com.facebook.** { *; }
-dontwarn com.facebook.**

# ========================
# Security Crypto
# ========================

-keep class androidx.security.crypto.** { *; }

# ========================
# Remove Logging
# ========================

-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# ========================
# Enum
# ========================

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ========================
# Parcelable
# ========================

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ========================
# Serializable
# ========================

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ========================
# Native Methods
# ========================

-keepclasseswithmembernames class * {
    native <methods>;
}

# ========================
# Views with onClick
# ========================

-keepclassmembers class * extends android.view.View {
    void set*(***);
    *** get*();
}
