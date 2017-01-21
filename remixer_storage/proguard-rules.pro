# Necessary to use DatabaseReference.setValue(Object), see
# https://firebase.google.com/docs/database/android/start/
-keepattributes Signature

# keep all the serialization-related classes.
-keepclassmembers class com.google.android.libraries.remixer.serialization.** {
  *;
}
