########################################
# Keep debugging information
########################################
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

########################################
# Gson
########################################
-keepattributes Signature
-keepattributes *Annotation*

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

########################################
# Retrofit
########################################
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

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