# Remixer

Remixer is an library that allows you to dynamically adjust UI settings during the development process.

## Project structure

The project is defined as a gradle project with submodules.

* remixer_core: The core framework code, has no dependencies on the android framework and contains the core logic to run callbacks on changes to a Remix, etc. Most of the base classes are here
  * main: The code for the core framework
  * tests: JUnit tests for the Remixer code.
* remixer_ui: The code that shows remixes in the UI.
  * main
    * `com.google.android.libraries.remixer.view` is  the code to display the UI as a BottomSheetFragmentDialog
    * `com.google.android.libraries.remixer.widget` is a family of widgets that can display each individual Remix.
    * `com.google.android.libraries.remixer.widget.RemixWidget` is an Interface to implement such widgets. Necessary if you want to provide different widgets.
  * tests: JUnit/Robolectric tests
* remixer_example: This is an example app.
  * main: the code for the example app
    

## Building

1. Clone the repository
   ```git clone https://github.com/material-foundation/material-remixer-android.git```
2. You have two options here:
  1. Open it on Android Studio and build from there.
  2. run `./gradlew build`  on your terminal (`gradlew.bat build` on Windows).
   
## Installing the example app

If you're reading this you're probably installing the app from the terminal as opposed to Android Studio.

```adb install -r remixer_example/build/outputs/apk/app-debug.apk```


