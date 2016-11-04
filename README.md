[![Build Status](https://travis-ci.org/material-motion/material-motion-runtime-android.svg?branch=develop)](https://travis-ci.org/material-motion/material-motion-runtime-android) [![codecov](https://codecov.io/gh/material-motion/material-motion-runtime-android/branch/develop/graph/badge.svg)](https://codecov.io/gh/material-motion/material-motion-runtime-android)
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

## Try it in your app!

__Disclaimer:__ Remixer still hasn't reached a stage that we consider is stable enough to commit to the current status of the API, it will be evolving quickly and we may commit breaking changes every once in a while. _That said_, we would love to have you try it out and tell us what you think is missing and what you'd like us to focus on.

### Set up dependencies

Using gradle it's super easy to start using Remixer following these instructions.

In your main build.gradle file make sure you have the following dependencies and repositories set up:

```gradle
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And in your modules, apply the `android-apt` plugin and add the remixer dependencies:
```gradle
apply plugin: 'android-apt'

dependencies {
    compile 'com.github.material-foundation.material-remixer-android:remixer_core:develop-SNAPSHOT'
    compile 'com.github.material-foundation.material-remixer-android:remixer_ui:develop-SNAPSHOT'
    compile 'com.github.material-foundation.material-remixer-android:remixer_storage:develop-SNAPSHOT'
    provided 'com.github.material-foundation.material-remixer-android:remixer_annotation:develop-SNAPSHOT'
}
```

Notice the dependency on `remixer_annotation` is a `provided` clause instead of `compile`, this is on purpose as this is not a regular dependency but a compiler plugin.

### Global remixer set up
If you have not subclassed the application class it is recommended you do it since this is a one-time global initialization.

In your application class you need to add RemixerCallbacks as an ActivityLifecycleCallbacks instance, so Remixer knows to remove old variables and triggers when activities are destroyed, to avoid leaks.
```java
class MyApplication extends android.app.Application {
  @Override
  public void onCreate() {
    super.onCreate();
    registerActivityLifecycleCallbacks(RemixerCallbacks.getInstance());
  }
}
```

### How to use it in an activity

__Only in the activities where you're using remixer__

You need to add a few lines at the end of your `Activity.onCreate()`

1. `RemixerBinder.bind(this);` creates, initializes and sets up all the Variables and trigger you define in this activity.
2. `RemixerFragment remixerFragment = RemixerFragment.newInstance();` creates the fragment that will be shown when remixer is invoked, then you need at least one of the following:
  - A variation of `remixerFragment.attachToGesture(this, Direction.UP, 3);`, this example ties showing the Remixer Fragment on a 3-finger swipe up.
  - `remixerFragment.attachToButton(this, someButtonObject);`, this makes the OnClickListener for a button open the Remixer fragment.

Your `Activity.onCreate` may look like this:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //...
  remixerButton = (Button) findViewById(R.id.button);
  RemixerBinder.bind(this);
  RemixerFragment remixerFragment = RemixerFragment.newInstance();
  remixerFragment.attachToGesture(this, Direction.UP, 3);
  remixerFragment.attachToButton(this, remixerButton);
}
```

#### Define variables

In order to define variables you only need to write methods that take one argument of the correct type and annotate them. The methods contain your logic to handle changes to these variables (update the UI accordingly). You can rest assured those methods will run in the main UI thread.

There are a few very simple examples here, but you should look at the [example](https://github.com/material-foundation/material-remixer-android/blob/develop/remixer_example/src/main/java/com/google/android/apps/remixer/MainActivity.java) [activities](https://github.com/material-foundation/material-remixer-android/blob/develop/remixer_example/src/main/java/com/google/android/apps/remixer/BoxActivity.java) and [documentation for these annotations](https://github.com/material-foundation/material-remixer-android/tree/develop/remixer_core/src/main/java/com/google/android/libraries/remixer/annotation) for more information.

Once you add your annotated methods and build you should be able to invoke remixer (by doing a 3 finger swipe or clicking a button, depending on how you configured it in the section above), and tweak the variables or trigger the events guarded by the trigger.

##### Range variables
__Note:__ for the time being they only support Integers, not floats or doubles.

They support the following properties:

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `defaultValue` the initial value, if not set assumes 0 or `minValue` if 0 is out of range.
- `minValue` the minimum value, if not set assumes 0
- `maxValue` the maximum value, if not set assumes 100
- `increment` the increment between two steps of the range, 1 by default.
- `layoutId` a layoutId to display this, must implement RemixerItemWidget. It assumes a sensible default if unset.

A Range variable that goes from 15 to 70 and starts at 20 by default:
```java
@RangeVariableMethod(
    minValue = 15, maxValue = 70, defaultValue = 20)
public void setFontSize(Integer fontSize) {
}
```

(Notice how integer variables take `Integer` and not `int`, this is a limitation on the Java type system)

##### Boolean Variable
They support the following properties:

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `defaultValue` the initial value, if not set assumes `false`
- `layoutId` a layoutId to display this, must implement RemixerItemWidget. It assumes a sensible default if unset.

A Boolean variable that has true as a default value:
```java
@BooleanVariableMethod(defaultValue = true, key = "someRemixerKey")
public void setUseNewDialog(Boolean useNewDialog) {
}
```

(Notice how boolean variables take `Boolean` and not `boolean`, this is a limitation on the Java type system)

##### String List Variable
They support the following properties:

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `defaultValue` the initial value, if not set assumes the first in the `possibleValues` list
- `possibleValues` the list of possible values.
- `layoutId` a layoutId to display this, must implement RemixerItemWidget. It assumes a sensible default if unset.

A String List variable that sets fonts from a list and defaults to the first in the list:
```java
@StringListVariableMethod(
    title = "Title font",
    possibleValues = {"Roboto", "Roboto Mono", "Comic Sans MS"})
public void setTitleFont(String fontName) {
}
```

##### String Variable

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `defaultValue` the initial value, if not set assumes the empty string.
- `layoutId` a layoutId to display this, must implement RemixerItemWidget. It assumes a sensible default if unset.

A String variable that sets freeform example text:
```java
@StringVariableMethod
public void setExampleText(String exampleText) {
}
```

##### Integer List Variable

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `defaultValue` the initial value, if not set assumes the first in the `possibleValues` list
- `possibleValues` the list of possible values.
- `layoutId` a layoutId to display this, must implement RemixerItemWidget. It assumes a sensible default if unset.

A variable that lets you pick colors from a list, this example uses a custom layout id to guarantee that it's treated as a Color:
```java
@IntegerListVariableMethod(
    title = "Title Color",
    possibleValues = {Color.parseColor("#000000"), Color.parseColor("#DCDCDC")},
    layoutId = R.layout.color_list_variable_widget)
public void setTitleColor(Integer color) {
}
```

##### Trigger

- `key` the key for this trigger, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the trigger, if not set assumes `key`
- `layoutId` a layoutId to display this, must implement RemixerItemWidget. It assumes a sensible default if unset.

A trigger to simulate an event happening:
```java
@TriggerMethod
public void simulateConnectionFailure() {
}
```

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

### Installing the example app

If you're reading this you're probably installing the app from the terminal as opposed to Android Studio.
```adb install -r remixer_example/build/outputs/apk/remixer_example-debug.apk```

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
