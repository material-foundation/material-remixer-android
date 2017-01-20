# Add this global rule
-keepattributes Signature

# keep all the serialization-related classes.
-keepclassmembers class com.google.android.libraries.remixer.serialization.** {
  *;
}
