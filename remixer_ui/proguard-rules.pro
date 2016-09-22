
-keep @android.support.annotation.Keep class *

-keepclassmembers class * {
   @android.support.annotation.Keep *;
}

-keep interface com.google.android.libraries.remixer.annotation.RemixerBinder$Binder

-keepnames class * {
   @com.google.android.libraries.remixer.annotation.RemixerInstance *;
}

-keep class * implements com.google.android.libraries.remixer.annotation.RemixerBinder$Binder {
  *;
}
