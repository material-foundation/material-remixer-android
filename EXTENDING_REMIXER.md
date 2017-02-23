# Extending Remixer

There are several ways in which you can extend remixer:

- Add a new `DataType`:
  - Create a `ValueConverter` for the `DataType`
  - Set as default layoutIDs by calling `DataType.setLayoutIdForVariableType(Class<? extends Variable>, int)`
  - Register it at `Application.onCreate()` time
    - can be done in `RemixerInitialization.initRemixer()` if you're adding it at the Remixer level (forking/contributing)
  - **Note**: At the time only forks/contributions can add annotation processing support for new DataTypes. If it proves necessary we'll write extension points for the annotation processor. 
- Implement a new `RemixerWidget`
  - Optionally, set it as a default Layout Id for a `DataType`/`Variable class` combination by calling `DataType.setLayoutIdForVariableType`. You can do it in `RemixerInitialization.initRemixer()` if forking/contributing.

**Notice that if you plan to use the Firebase Remote Controller functionality, you need to replicate this work in the material-remixer-js and material-remixer-remote-web projects as well**. It is out of scope for this document to explain how to do it in those projects.

## Adding a new DataType

In order to add a new data type to Remixer, you need to construct a static instance of the DataType class...

```java
public static final DataType<RT, ST> MY_NEW_TYPE = new DataType<>(
    "myNewType",
    RT.class,
    ST.class,
    new MyNewTypeValueConverter("myNewType"));
```

... and register it as a RemixerType somewhere that only gets called once (preferably during your `Application.onCreate()`)...

```java
public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Remixer.registerDataType(MY_NEW_TYPE);
  }
}
```

Now let's dissect the first statement, the `new DataType<>(...)`:

1. It has two generic parameters, RT and ST, the RuntimeType and the SerializedType. The former is used for callbacks during runtime, and the latter is used to serialize, store data and (potentially) sync it to Firebase. They can be exactly the same type, and they usually are.
2. It has a unique identifier string, in the example it's "myNewType". It can be whatever you want but current identifier strings are "boolean", "color", "number" and "string".
3. You need to write a subclass of `ValueConverter` which converts between the RuntimeType and the SerializedType and performs other serialization-related tasks.


Now you can use it with any of the variable classes using the appropriate Builder. Just make sure to call `setDataType(MY_DATA_TYPE)` and you're done.

## Add a new Variable subclass

Variable subclasses represent constraints on what values the variable can take.

1. Create a subclass of Variable.
2. Override its `checkValue(T)` method.
3. Override its `getSerializableConstraints()` method with a new constant constraint string that is unique to this class. 
4. Add new fields to `StoredVariable` representing new data required to represent the new variable subclass.
5. Make sure you update `ValueConverter`'s `fromVariable(Variable<?>)`, `serialize(StoredVariable<SerializableType>)` and `deserialize(JsonElement)` methods to match the new fields.

## Adding new annotation support

If you add a new type or variable you may want to make it easier to use by adding annotation support. This is easy if you are forking or contributing to the android remixer repository, but otherwise we do not currently offer a way to extend the annnotation processor. We may if this becomes a problem down the line.

1. Subclass `MethodAnnotation` to handle your new annotation classes.
2. Add a new enum item per new annotation you're supporting to `SupportedMethodAnnotation`
3. Add lots of tests, these errors are hard to debug, so please do your best to keep coverage high.

## Implement a new RemixerWidget

**Note:** Remixer relies on inflating widgets by layout id, so these cannot be pure-java, they need to have a layout resource associated with them.

1. Write a new class that extends `android.view.ViewGroup` (usually either `LinearLayout` or `RelativeLayout`) and implements `RemixerWidget`
2. Add a new layout resource whose element is of the class you just created.
3. Optionally, set it as a default Layout Id for a `DataType`/`Variable class` combination by calling `DataType.setLayoutIdForVariableType(Class<? extends Variable>, int)`. You can do it in `RemixerInitialization.initRemixer()` if forking/contributing.

Even if you do not set it as a default layout anywhere, you can still force to use it by setting the layoutId property on a variable.
