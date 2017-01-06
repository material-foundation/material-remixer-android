
-keep @android.support.annotation.Keep class *

-keepclassmembers class * {
   @android.support.annotation.Keep *;
}

# Necessary to deal with support library having references to methods that are since removed.
# i.e. void android.app.Notification.setLatestEventInfo(android.content.Context,
# java.lang.CharSequence,java.lang.CharSequence,android.app.PendingIntent).
# If this dontwarn clause is not there proguard fails.
-dontwarn android.support.v4.**

-keep interface com.google.android.libraries.remixer.annotation.RemixerBinder$Binder

-keepnames class * {
   @com.google.android.libraries.remixer.annotation.BooleanVariableMethod <methods>;
}
-keepnames class * {
   @com.google.android.libraries.remixer.annotation.ColorListVariableMethod <methods>;
}
-keepnames class * {
   @com.google.android.libraries.remixer.annotation.RangeVariableMethod <methods>;
}
-keepnames class * {
   @com.google.android.libraries.remixer.annotation.StringVariableMethod <methods>;
}
-keepnames class * {
   @com.google.android.libraries.remixer.annotation.StringListVariableMethod <methods>;
}

-keep class * implements com.google.android.libraries.remixer.annotation.RemixerBinder$Binder {
  *;
}
