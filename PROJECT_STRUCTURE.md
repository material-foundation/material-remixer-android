![Remixer](https://cdn.rawgit.com/material-foundation/material-remixer/master/docs/assets/lockup_remixer_icon_horizontal_dark_small.svg)

# Project structure

Remixer is built as a gradle project with several submodules. If you are interested in contributing you should know the roles of each submodule to be able to put things in the right place.

## Core and Annotations

Remixer has a core library, `remixer_core`, where the main logic for Remixer is, here is where you can find
- DataTypes supported by Remixer `com.google.android.libraries.remixer.DataType`
- Serialization logic `package com.google.android.libraries.remixer.serialization`
- All variable logic `com.google.android.libraries.remixer.Variable`, `com.google.android.libraries.remixer.ItemListVariable`, `com.google.android.libraries.remixer.RangeVariable`
- The `com.google.android.libraries.remixer.Remixer` class which aggregates all variables and `package com.google.android.libraries.remixer.sync` which takes care of keeping values in sync across different contexts.
  - `com.google.android.libraries.remixer.sync.LocalValueSyncing` is a non-persistent version of a `com.google.android.libraries.remixer.sync.SynchronizationMechanism`. Persistent versions are available in the `remixer_storage` submodule, those depend on android-specific classes which are unavailable on this project, because of the reasons explained in the next section.
- Declaration of all annotations exposed by Remixer `package com.google.android.libraries.remixer.annotation` and the annotation binding classes.

The annotation-processing library, `remixer_annotation`, contains code that should only be run by the Java Compiler (and should never be in the classpath for an APK), this code generates Remixer variables from annotations in `com.google.android.libraries.remixer.annotation`.


### Constraints on dependencies

**TL;DR: If you contribute to Remixer, you need to know the `remixer_core` submodule cannot depend on any android classes**

Remixer uses annotations heavily, and annotation processing on Android is very difficult. The Android SDK does not include classes required for annotation processing, therefore if you depend on the Android SDK, you cannot use annotation processing. For this reason there are two submodules that are Java libraries instead of Android libraries, `remixer_core` and `remixer_annotation`.

This means that the annotation processing code (in `remixer_annotation`) is unable to generate code that uses any Android class (i.e. can't generate `OnClickListener`s, or use `Actvity` in generated code, etc.) <sup id="a1">[1](#f1)</sup>. Furthermore, in some cases, where Remixer needs a Context (which should naturally be an instance of `android.app.Activity` or at least `android.content.Context`) we take `java.lang.Object` instead. It is generally very well explained in comments that this is the case.


### How the annotation processor works

All the annotation processor classes are in the `com.google.android.libraries.remixer.annotation.processor` package.

The entry point to the Remixer Annotation Processor is the `RemixerAnnotationProcessor` class. This class finds all annotations in your code that are registered in `SupportedMethodAnnotation` and creates a `MethodAnnotation` object for each annotation found and an `AnnotatedClass` object for each class that has at least one of the supported annotations.

`AnnotatedClass` is responsible for writing out a helper class that contains all generated callbacks for annotations in the corresponding source class, and `bind(SourceClass activity)` method that generates all the remixer variable instances. This generated class is deterministically named `<SourceClass>_RemixerBinder` and implements `com.google.android.libraries.remixer.annotation.RemixerBinder.Binder`. The generated class is found at runtime through reflection and it depends on the names for the source class and the generated class to be kept intact by proguard, which is why the following proguard rules are present in `remixer_ui` <sup id="a2">[2](#f2)</sup>:
  
```
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
```

`AnnotatedClass` keeps a list of associated `MethodAnnotation`s that in turn generate each of the callback nested classes that act as the direct remixer callbacks. There is a hierarchy of `MethodAnnotation` classes for differently constrained Variables (RangeVariables, ItemListVariables or plain Variables).

The annotation processor uses [Javapoet](https://github.com/square/javapoet) to generate the code which  offers an easy to read and maintain syntax for code generation.

## Storage submodule.

The `remixer_storage` submodule currently contains two implementations of `remixer_core`'s `com.google.android.libraries.remixer.sync.SynchronizationMechanism`:
- `com.google.android.libraries.remixer.storage.LocalStorage` writes to a shared preferences file each time a value is modified. It uses `SharedPreferences.Editor#apply()` to commit the changes, which means that if there are many changes one after the other it would (probably) hit the disk only once, after all of them complete, and it doesn't block the UI thread at all.
- `com.google.android.libraries.remixer.storage.FirebaseRemoteController`: <sup id="a3">[3](#f3)</sup> Uses firebase for storage and synchronization back to the device. This is necessary for the upcoming firebase Remote Controller functionality.

## UI submodule

The `remixer_ui` submodule contains the actual Remixer visible UI and a little of the "glue code" that makes everything seamless.
- `package com.google.android.libraries.remixer.ui.view` contains the classes necessary to use the `RemixerFragment` that shows all the widgets that change Remixer.
  - The `RemixerFragment` offers three ways to be triggered: bound to a multi-finger gesture using `com.google.android.libraries.remixer.ui.gesture.GestureListener`, bound to a device shake using `com.google.android.libraries.remixer.ui.gesture.ShakeListener` and bound to a button click.
- Specific widgets for each type of variable are found in `package com.google.android.libraries.remixer.ui.widget`.
- `com.google.android.libraries.remixer.ui.RemixerInitialization` provides a handy `initRemixer(Application)` method that correctly initializes remixer (registers the basic supported data types and their corresponding mappings to widgets, and registers for lifecycle events to avoid memory leaks).

Even if not using the Remixer UI directly, this submodule is necessary for the glue code (registering data types and avoiding memory leaks). These are kept in the UI module instead of the core module because they need access to both the Android SDK classes and specific classes from this module.

- - -

<b id="f1">1</b> Okay, technically it could generate code that used those classes, but since the tests for annotation-processors generally need compilation of sample code, generated code that uses those classes would not pass the tests since the classes wouldn't be in the classpath. [↩](#a1)

<b id="f2">2</b>TODO, need to investigate whether these can be successfully moved to `remixer_core` where they rightfully belong. They are located in the UI project since that one is an Android library instead of a plain Java library. [↩](#a2)

<b id="f2">2</b> Since this imports Firebase, you need to have a `google-services.json` file on your app module even if you're not using firebase. An empty file will do, or the example one we keep on `remixer_example/src/main/google-services.json`. We are investigating how to make this easier. [↩](#a2)
