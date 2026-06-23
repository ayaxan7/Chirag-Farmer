########################################
# Keep debugging information
########################################
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

########################################
# Gson
########################################
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep model classes used only as erased generic type params in AuthApiService
-keep class com.yash091099.ChiragFarmersApp.data.model.auth.SendOTPData { *; }
-keep class com.yash091099.ChiragFarmersApp.data.model.auth.VerifyOTPData { *; }
-keep class com.yash091099.ChiragFarmersApp.data.model.auth.UserDetailsData { *; }
-keep class com.yash091099.ChiragFarmersApp.data.model.auth.UpdateProfileData { *; }
-keep class com.yash091099.ChiragFarmersApp.data.model.auth.FarmerProfileData { *; }
-keep class com.yash091099.ChiragFarmersApp.data.model.auth.User { *; }

########################################
# Retrofit
########################################
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

########################################
# Kotlin Coroutines
########################################
-keep class kotlin.coroutines.** { *; }

########################################
# Parcelable
########################################
#-keep class ** implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}