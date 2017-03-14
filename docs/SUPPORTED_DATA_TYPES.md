
Table of Contents
=================
* [API Flavors](#api-flavors)
* [Constraints](#constraints)
   * [Unconstrained variables](#unconstrained-variables)
   * [Item-list variables](#item-list-variables)
   * [Range variables](#range-variables)
* [Data Types](#data-types)
   * [Boolean](#boolean)
   * [Color](#color)
   * [Number](#number)
   * [String](#string)

# Variables in Remixer

Variables are the cornerstone of Remixer, a change on a variable's _value_ (by the Remixer UI, remote controller or any other means) triggers the _callback_ (your code). Variables have a _data type_ and (optionally) a _constraint_ (which on Android are represented by subclasses of `com.google.android.libraries.remixer`).

Remixer has built-in support for 4 data types (Boolean, Color, Number and String), and you could add support for more types as explained in [Extending Remixer](EXTENDING_REMIXER.md).

Remixer guarantees that your callbacks are always called on the Main UI Thread for your application, but also makes no guarantee of thread-safety if you interact with remixer from other threads. Make sure you keep that in mind.

## API Flavors

First of all you need to know that there are two ways to use variables, either directly through an explicit API in the form of Builders (all in package `com.google.android.libraries.remixer`) or through a convenient set of annotations found in `com.google.android.libraries.remixer.annotations`.

**It is recommended you consistently use annotations unless you are extending Remixer**.

In the explicit API you need to implement the `com.google.android.libraries.remixer.Callback<T>` interface to implement your callback and specify the DataType in the builder calls. In the annotation-based API you need to apply the annotation to a public method with one argument of the correct type and at the end of the activity's `onCreate` method you call `RemixerBinder.bind(this)`.

There are a few very simple examples here of how to use both in the following sections, but you should look at the [example activities](https://github.com/material-foundation/material-remixer-android/blob/develop/remixer_example/src/main/java/com/google/android/apps/remixer/TransactionListActivity.java) and documentation for [the explicit API](https://github.com/material-foundation/material-remixer-android/tree/develop/remixer_core/src/main/java/com/google/android/libraries/remixer) and [these annotations](https://github.com/material-foundation/material-remixer-android/tree/develop/remixer_core/src/main/java/com/google/android/libraries/remixer/annotation) for more information.

## Constraints

### Unconstrained variables

Class: `com.google.android.libraries.remixer.Variable`

This is the base variable, it takes any possible value for the data type.

#### Properties

| Name | Description |
| ---- | ----------- |
| `key` | The key for this variable, you can use it to share the same value across activities, if not set it assumes the method name. |
| `title` | The displayable name of the variable, if not set assumes `key` |
| `context` | An activity where the variable is defined. Used to avoid memory leaks. |
| `initialValue` | The initial value. |
| `callback` | The method to call once the variable value changes. |
| `layoutId` | A layoutId to display this, must implement RemixerWidget. It can remain unset and it will use a sensible default in each **supported** case. |

#### Builder classes

| Class | Description |
| ----- | ----------- |
| `com.google.android.libraries.remixer.BaseVariableBuilder` | Can be used theoretically with any type. |
| `com.google.android.libraries.remixer.BooleanVariableBuilder` | Convenience Variable builder that assumes false to be the initial value if unset. |
| `com.google.android.libraries.remixer.StringVariableBuilder` | Convenience Variable builder that assumes the empty string to be the initial value if unset. |

#### Annotations

- `com.google.android.libraries.remixer.annotation.BooleanVariableMethod`
- `com.google.android.libraries.remixer.annotation.StringVariableMethod`

### Item-list variables

Class: `com.google.android.libraries.remixer.ItemListVariable`

These variables will throw an exception if the value is set to anything that isn't in its `limitedToValues` list.

#### Properties

This variable constraint has all of the properties of unconstrained variables in addition to:

| Name | Description |
| ---- | ----------- |
| `limitedToValues` | The list of values this variable is limited to take. |

#### Builder classes

| Class | Description |
| ----- | ----------- |
| `com.google.android.libraries.remixer.ItemListVariable.Builder` | Assumes the first value in the `limitedToValues` list to be the `initialValue` if it is unset. |

#### Annotations

- `com.google.android.libraries.remixer.annotation.ColorListVariableMethod`
- `com.google.android.libraries.remixer.annotation.NumberListVariableMethod`
- `com.google.android.libraries.remixer.annotation.StringListVariableMethod`

### Range variables

Class: `com.google.android.libraries.remixer.RangeVariable`

These variables are designed to work with Numbers only, and will throw an exception if the value is not within a specified range. Furthermore they have a concept of increments, so only values in the given increments are valid (for example, for a variable with {min: 0, increment: 5, max: 15}, 10 is a valid value, while 9 and 11 aren't)

#### Properties

This variable constraint has all of the properties of unconstrained variables in addition to:

| Name | Description |
| ---- | ----------- |
| `minValue` | The minimum value |
| `maxValue` | The maximum value |
| `increment` | The increment between two steps of the range, 1 by default. |

#### Builder classes

| Class | Description |
| ----- | ----------- |
| `com.google.android.libraries.remixer.RangeVariable.Builder` | No Description |

#### Annotations

- `com.google.android.libraries.remixer.annotation.RangeVariableMethod`

## Data Types

Each data type has a type that represents it in runtime, a type that represents it while serialized (usually the same as the runtime one), and a converter between those types.

Furthermore, in order to display a Variable, there must be an appropriate widget for its combination of data type and constraints. Not all possible combinations are currently supported (or even make sense). This is further explained in the following sections.

### Boolean

The boolean data type is pretty self explanatory. Because the boolean data type has only two possible values, `true` and `false`, no constraints are supported.

| Name | Value |
| ---- | ----- |
| Runtime type | `java.lang.Boolean` |
| Serializable type | `java.lang.Boolean` |
| Converter | `com.google.android.libraries.remixer.serialization.converters.BooleanValueConverter` |
| Supported constraints | Unconstrained variables (via `com.google.android.libraries.remixer.ui.widget.BooleanVariableWidget`) |

#### Explicit API Boolean Variable

```java
Variable<Boolean> variable = new BooleanVariableBuilder()
    .setKey("boolean")
    .setContext(this)
    .setCallback(new Callback<Boolean>() {
      @Override
      public void onValueSet(Boolean booleanValue) {
        // ...
      }
    })
    .build()
Remixer.getInstance().addItem(variable);
```

#### Annotation-based API Boolean Variable

A Boolean variable that has true as its initial value:
```java
@BooleanVariableMethod(initialValue = true, key = "someRemixerKey")
public void setUseNewDialog(Boolean useNewDialog) {
}
```

### Color
The Color data type lets you manipulate colors in the UI.

| Name | Value |
| ---- | ----- |
| Runtime type | `java.lang.Integer` It uses integer as its runtime type as that is the android-native way to represent colors. |
| Serializable type | `com.google.android.libraries.remixer.serialization.SerializedColor` it uses an intermediate type to serialize to be cross-platform friendly. |
| Converter | `com.google.android.libraries.remixer.serialization.converters.ColorValueConverter` |
| Supported constraint | Item List Variables (via `com.google.android.libraries.remixer.ui.widget.ColorListVariableWidget`) <br /> <br /> Theoretically we could support unconstrained variables, but we haven't built a good color picker. You're welcome to [contribute one](CONTRIBUTING.md).|

#### Explicit API Color List Variable

```java
ItemListVariable<Integer> variable = new ItemListVariable.Builder<Integer>()
    .setLimitedToValues(new Integer[]{0xFF0000, 0x00FF00, 0x0000FF})
    .setKey("color")
    .setContext(this)
    .setDataType(DataType.COLOR)
    .setCallback(new Callback<Integer>() {
      @Override
      public void onValueSet(Integer color) {
        // ...
      }
    })
    .build()
Remixer.getInstance().addItem(variable);
```

#### Annotation-based Color List Variable

```java
@ColorListVariableMethod(
    limitedToValues = {Color.parseColor("#000000"), Color.parseColor("#DCDCDC")})
public void setTitleColor(Integer color) {
}
```

### Number

Remixer only supports `Float` numbers. These should work for most cases anyway.

| Name | Value |
| ---- | ----- |
| Runtime type | `java.lang.Float` |
| Serializable type | `java.lang.Float` |
| Converter | `com.google.android.libraries.remixer.serialization.converters.NumberValueConverter` |
| Supported constraint |  Item List Variables (via `com.google.android.libraries.remixer.ui.widget.ItemListVariableWidget`) |
| Supported constraint |  Range Variables (via `com.google.android.libraries.remixer.ui.widget.SeekBarRangeVariableWidget`) |

Theoretically we could support Unconstrained Variables if we write a **usable** control that handles non-number values correctly. You're welcome to [contribute an implementation](CONTRIBUTING.md). |


#### Explicit API Number List Variable

```java
ItemListVariable<Integer> variable = new ItemListVariable.Builder<Integer>()
    .setLimitedToValues(new Integer[]{1, 2, 3, 5, 8, 13, 21, 34})
    .setKey("number")
    .setContext(this)
    .setDataType(DataType.NUMBER)
    .setCallback(new Callback<Integer>() {
      @Override
      public void onValueSet(Integer number) {
        // ...
      }
    })
    .build()
Remixer.getInstance().addItem(variable);
```

#### Annotation-based Number List Variable

```java
@NumberListVariableMethod(limitedToValues = {1, 2, 3, 5, 8, 13, 21, 34})
public void setFavoriteFibonacciNumber(Integer fibonacci) {
}
```

#### Explicit API Range Variable

```java
ItemListVariable<Integer> variable = new ItemListVariable.Builder<Integer>()
    .setLimitedToValues(new Integer[]{1, 2, 3, 5, 8, 13, 21, 34})
    .setKey("number")
    .setContext(this)
    .setDataType(DataType.NUMBER)
    .setCallback(new Callback<Integer>() {
      @Override
      public void onValueSet(Integer number) {
        // ...
      }
    })
    .build()
Remixer.getInstance().addItem(variable);
```

#### Annotation-based Range Variable

A Range variable that goes from 15 to 70 and starts at 20 by default:
```java
@RangeVariableMethod(minValue = 15, maxValue = 70, initialValue = 20)
public void setFontSize(Integer fontSize) {
}
```

### String

The Color data type lets you manipulate colors in the UI.

| Name | Value |
| ---- | ----- |
| Runtime type | `java.lang.String` |
| Serializable type | `java.lang.String` |
| Converter | `com.google.android.libraries.remixer.serialization.converters.StringValueConverter` |
| Supported constraints | Item List Variables (via `com.google.android.libraries.remixer.ui.widget.ItemListVariableWidget`) |
| Supported constraints | Unconstrained Variables (via `com.google.android.libraries.remixer.ui.widget.StringVariableWidget`) |


#### Explicit API String List Variable

```java
ItemListVariable<String> variable = new ItemListVariable.Builder<Integer>()
    .setLimitedToValues(new String[]{"Roboto", "Roboto Mono", "Comic Sans MS"})
    .setKey("font")
    .setContext(this)
    .setDataType(DataType.STRING)
    .setCallback(new Callback<String>() {
      @Override
      public void onValueSet(String fontName) {
        // ...
      }
    })
    .build()
Remixer.getInstance().addItem(variable);
```

#### Annotation-based String List Variable

A String List variable that sets fonts from a list and defaults to the first in the list:
```java
@StringListVariableMethod(limitedToValues = {"Roboto", "Roboto Mono", "Comic Sans MS"})
public void setTitleFont(String fontName) {
}
```

#### Explicit API String Variable

```java
Variable<String> variable = new StringVariableBuilder()
    .setKey("string")
    .setContext(this)
    .setCallback(new Callback<String>() {
      @Override
      public void onValueSet(String string) {
        // ...
      }
    })
    .build()
Remixer.getInstance().addItem(variable);
```

#### Annotation-based String Variable

A String variable that sets freeform example text:
```java
@StringVariableMethod
public void setExampleText(String exampleText) {
}
```
