# ![Remixer](https://cdn.rawgit.com/material-foundation/material-remixer/master/docs/assets/lockup_remixer_icon_horizontal_dark_small.svg)

Remixer helps teams use and refine design specs by providing an abstraction for these values that is accessible and configurable from both inside and outside the app itself.

This SDK for Android is currently in development.

**New to Remixer?** Visit our [main repo](https://github.com/material-foundation/material-remixer) to get a full description of what it is and how it works.
- - -

## Project structure

The project is defined as a gradle project with submodules.

* remixer_core: The core framework code, has no dependencies on the android framework and contains the core logic to run callbacks on changes to a Variable, etc. Most of the base classes are here
  * main: The code for the core framework
  * tests: JUnit tests for the Remixer code.
* remixer_ui: The code that shows remixes in the UI.
  * main
    * `com.google.android.libraries.remixer.ui.view` is  the code to display the UI as a BottomSheetFragmentDialog
    * `com.google.android.libraries.remixer.ui.widget` is a family of widgets that can display each individual Remixer item (variable or trigger).
    * `com.google.android.libraries.remixer.ui.widget.RemixerItemWidget` is an Interface to implement such widgets. Necessary if you want to provide different widgets.
    * `com.google.android.libraries.remixer.ui.RemixerCallbacks` is an implementation of `Application.ActivityLifecycleCallbacks` that clears up callbacks once the corresponding activity is destroyed so it doesn't leak. It needs to be registered in the `Application.onCreate()` method.
  * tests: JUnit/Robolectric tests.
* remixer_annotation: An annotation processor to make it easier to add remixes to your code.
  * main: The annotation processing code.
  * tests: JUnit tests based on actual classes to compile.
* remixer_example: This is an example app.
  * main: the code for the example app

## Building

1. Clone the repository
   ```git clone https://github.com/material-foundation/material-remixer-android.git```
2. You have two options here:
  1. Open it on Android Studio and build from there.
  2. run `./gradlew build`  on your terminal (`gradlew.bat build` on Windows).
    * This assumes that you have a copy of the android SDK and,
    * That you have exported a `ANDROID_HOME` environment variable that points to your SDK install (the directory that contains subdirectories such as `tools`, `platform-tools`, etc.)
      * If you use Android Studio on a mac that defaults to `/Users/<yourusername>/Library/Android/sdk`
      * In this case you can put `export ANDROID_HOME=/Users/<yourusername>/Library/Android/sdk` in your `~/.profile` or `~/.bash_profile` as appropriate.)

## Installing the example app

If you're reading this you're probably installing the app from the terminal as opposed to Android Studio.
```adb install -r remixer_example/build/outputs/apk/remixer_example-debug.apk```

## Try it in your app!

__Disclaimer:__ Remixer still hasn't reached a stage that we consider is stable enough to commit to the current status of the API, it will be evolving quickly and we may commit breaking changes every once in a while. _That said_, we would love to have you try it out and tell us what you think is missing and what you'd like us to focus on.

Using gradle it's super easy to start using Remixer following these instructions.

In your main build.gradle file make sure you have the following repositories set up:

```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And in your modules, add the dependencies you need from remixer, most likely these:
```gradle
dependencies {
    compile 'com.github.material-foundation.material-remixer-android:remixer_core:develop-SNAPSHOT'
    compile 'com.github.material-foundation.material-remixer-android:remixer_ui:develop-SNAPSHOT'
    compile 'com.github.material-foundation.material-remixer-android:remixer_storage:develop-SNAPSHOT'
}
```

### If you're using the annotation-based API

_(You most-likely want to do this)_. You need to add a couple more dependencies.

In your main build.gradle add `classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'`, probably leaving your dependencies like this:
```gradle
buildscript {
  dependencies {
    classpath 'com.android.tools.build:gradle:2.2.0'
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}
```

On the modules where you will be using Remixer `apply plugin: 'android-apt'`, and add an extra dependency on `provided 'com.github.material-foundation.material-remixer-android:remixer_annotation:develop-SNAPSHOT'`. Notice how the dependency is a `provided` clause instead of `compile`, this is on purpose as this is not a regular dependency but a compiler plugin.

## Repositories

Platform specific libraries and tools can be found in the following GitHub repos:

- [iOS](https://github.com/material-foundation/material-remixer-ios) - Remixer for iOS.
- Web - Remixer for Web (available soon).
- Dashboard - Remixer web dashboard for all platforms (available soon).

## Is Material Foundation affiliated with Google?

Yes, the [Material Foundation](https://github.com/material-foundation) organization is one of Google's new homes for tools and frameworks related to our [Material Design](https://material.io) system. Please check out our blog post [Design is Never Done](https://design.google.com/articles/design-is-never-done/) for more information regarding Material Design and how Remixer integrates with the system.

## Contributing

We gladly welcome contributions! If you have found a bug, have questions, or wish to contribute, please follow our [Contributing Guidelines](https://github.com/material-foundation/material-remixer-android/blob/develop/CONTRIBUTING.md).

## License

© Google, 2016. Licensed under an [Apache-2](https://github.com/material-foundation/material-remixer-android/blob/develop/LICENSE) license.
