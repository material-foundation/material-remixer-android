![Remixer](https://cdn.rawgit.com/material-foundation/material-remixer/master/docs/assets/lockup_remixer_icon_horizontal_dark_small.svg)

[![TravisCI Build Status](https://travis-ci.org/material-foundation/material-remixer-android.svg?branch=develop)](https://travis-ci.org/material-foundation/material-remixer-android) [![CircleCI Build Status](https://circleci.com/gh/material-foundation/material-remixer-android.svg?style=svg)](https://circleci.com/gh/material-foundation/material-remixer-android) [![codecov](https://codecov.io/gh/material-foundation/material-remixer-android/branch/develop/graph/badge.svg)](https://codecov.io/gh/material-foundation/material-remixer-android)

Remixer helps teams use and refine design specs by providing an abstraction for these values that is accessible and configurable from both inside and outside the app itself.

This SDK for Android is currently in development.

**New to Remixer?** Visit our [main repo](https://github.com/material-foundation/material-remixer) to get a full description of what it is and how it works.

- - -

1. [Getting started](GETTING_STARTED.md)
2. [Configure the UI](CONFIGURE_UI.md)
2. [Project structure](PROJECT_STRUCTURE.md)

- - -

#### Define variables

In order to define variables you only need to write methods that take one argument of the correct type and annotate them. The methods contain your logic to handle changes to these variables (update the UI accordingly). You can rest assured those methods will run in the main UI thread.

There are a few very simple examples here, but you should look at the [example](https://github.com/material-foundation/material-remixer-android/blob/develop/remixer_example/src/main/java/com/google/android/apps/remixer/MainActivity.java) [activities](https://github.com/material-foundation/material-remixer-android/blob/develop/remixer_example/src/main/java/com/google/android/apps/remixer/BoxActivity.java) and [documentation for these annotations](https://github.com/material-foundation/material-remixer-android/tree/develop/remixer_core/src/main/java/com/google/android/libraries/remixer/annotation) for more information.

Once you add your annotated methods and build you should be able to invoke remixer (by doing a 3 finger swipe or clicking a button, depending on how you configured it in the section above), and tweak the variables.

##### Range variables
__Note:__ for the time being they only support Integers, not floats or doubles.

They support the following properties:

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `initialValue` the initial value, if not set assumes 0 or `minValue` if 0 is out of range.
- `minValue` the minimum value, if not set assumes 0
- `maxValue` the maximum value, if not set assumes 100
- `increment` the increment between two steps of the range, 1 by default.
- `layoutId` a layoutId to display this, must implement RemixerWidget. It assumes a sensible default if unset.

A Range variable that goes from 15 to 70 and starts at 20 by default:
```java
@RangeVariableMethod(
    minValue = 15, maxValue = 70, initialValue = 20)
public void setFontSize(Integer fontSize) {
}
```

(Notice how integer variables take `Integer` and not `int`, this is a limitation on the Java type system)

##### Boolean Variable
They support the following properties:

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `initialValue` the initial value, if not set assumes `false`
- `layoutId` a layoutId to display this, must implement RemixerWidget. It assumes a sensible default if unset.

A Boolean variable that has true as its initial value:
```java
@BooleanVariableMethod(initialValue = true, key = "someRemixerKey")
public void setUseNewDialog(Boolean useNewDialog) {
}
```

(Notice how boolean variables take `Boolean` and not `boolean`, this is a limitation on the Java type system)

##### String List Variable
They support the following properties:

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `initialValue` the initial value, if not set assumes the first in the `limitedToValues` list
- `limitedToValues` the list of values this variable is limited to take.
- `layoutId` a layoutId to display this, must implement RemixerWidget. It assumes a sensible default if unset.

A String List variable that sets fonts from a list and defaults to the first in the list:
```java
@StringListVariableMethod(
    title = "Title font",
    limitedToValues = {"Roboto", "Roboto Mono", "Comic Sans MS"})
public void setTitleFont(String fontName) {
}
```

##### String Variable

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `initialValue` the initial value, if not set assumes the empty string.
- `layoutId` a layoutId to display this, must implement RemixerWidget. It assumes a sensible default if unset.

A String variable that sets freeform example text:
```java
@StringVariableMethod
public void setExampleText(String exampleText) {
}
```

##### Number List Variable

- `key` the key for this variable, you can use it to share the same value across activities, if not set it assumes the method name.
- `title` the displayable name of the variable, if not set assumes `key`
- `initialValue` the initial value, if not set assumes the first in the `limitedToValues` list
- `limitedToValues` the list of values this variable is limited to take.
- `layoutId` a layoutId to display this, must implement RemixerWidget. It assumes a sensible default if unset.

A variable that lets you pick colors from a list, this example uses a custom layout id to guarantee that it's treated as a Color:
```java
@NumberListVariableMethod(
    title = "Title Color",
    limitedToValues = {Color.parseColor("#000000"), Color.parseColor("#DCDCDC")},
    layoutId = R.layout.color_list_variable_widget)
public void setTitleColor(Integer color) {
}
```

## State of development

Visit our [State of Development](https://github.com/material-foundation/material-remixer/wiki/State-of-Development) wiki for the current roadmap and status of development for each platform.

## Repositories

The main Remixer GitHub repo for documentation, project tracking, and general information:
- [Remixer docs](https://github.com/material-foundation/material-remixer)

Platform specific libraries and tools can be found in the following GitHub repos:

- [iOS](https://github.com/material-foundation/material-remixer-ios) - Remixer for iOS.
- [Web](https://github.com/material-foundation/material-remixer-web) - Remixer for Web.
- Web Remote - Remixer web remote controller for all platforms (available soon).

## Is material-foundation affiliated with Google?

Yes, the [material-foundation](https://github.com/material-foundation) organization is one of Google's new homes for tools and frameworks related to our [Material Design](https://material.io) system. Please check out our blog post [Design is Never Done](https://design.google.com/articles/design-is-never-done/) for more information regarding Material Design and how Remixer integrates with the system.

## Contributing

We gladly welcome contributions! If you have found a bug, have questions, or wish to contribute, please follow our [Contributing Guidelines](CONTRIBUTING.md) and read the [High-level Project Structure](PROJECT_STRUCTURE.md).

## License

© Google, 2016. Licensed under an [Apache-2](https://github.com/material-foundation/material-remixer-android/blob/develop/LICENSE) license.
