/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.annotation.processor;

import com.google.android.libraries.remixer.Callback;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Variable;
import com.google.common.base.Strings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.Locale;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * A method annotated by one of the *Method annotation.
 *
 * <p>Any *Method annotation must have a corresponding subclass of this class and it must
 * implement {@link #getKey()}, {@link #getVariableType()}, and
 * {@link #addSetupStatements(MethodSpec.Builder)}.
 */
abstract class MethodAnnotation {

  /**
   * Suffix to append to a variable name to hold the callback to be used by the variable.
   */
  static final String CALLBACK_NAME_SUFFIX = "_callback";
  /**
   * Suffix to append to a variable that holds the generated variable.
   */
  static final String REMIXER_ITEM_SUFFIX = "_remixer_item";
  /**
   * Statement to create the callback variable.
   *
   * <p>Would expand to something in the form of {@code CallbackType callbackVariable = new
   * CallbackType(activity)}
   */
  static final String NEW_CALLBACK_STATEMENT = "$L $L = new $L(activity)";
  /**
   * Statement to initialize a variable after it's been constructed.
   */
  static final String INIT_VARIABLE_STATEMENT = "$L.init()";
  /**
   * Statement to add a remixer item instance to the current remixer.
   */
  static final String ADD_VARIABLE_STATEMENT = "remixer.addItem($L)";
  protected static final String ACTIVITY_NAME = "activity";
  /**
   * The element where the annotation was found.
   */
  final ExecutableElement sourceMethod;
  /**
   * The type element which contains the annotated method.
   */
  private final TypeElement sourceClass;
  /**
   * The key for this Variable. If the annotation has an empty key then it uses the source method's
   * name as key.
   */
  protected final String key;
  /**
   * Title to display for this variable. If empty it will fall back to the key.
   */
  protected final String title;
  /**
   * The layoutId to pass to the constructor of the variable. This will be used to inflate the UI.
   */
  final int layoutId;
  /**
   * The builder type to instantiate for this method annotation.
   */
  protected final TypeName builderType;
  /**
   * Name of the variable holding the callback corresponding to this MethodAnnotation in the
   * generated setup code.
   */
  protected final String callbackName;
  /**
   * Name of the variable holding the RemixerItem builder/instance corresponding to this
   * MethodAnnotation in the generated setup code.
   */
  protected final String remixerItemName;
  /**
   * The name of the class to generate.
   */
  String generatedClassName;
  /**
   * The name of the class refered to by {@code sourceClass}, that is, the class where that contains
   * the annotated method.
   */
  private final TypeName sourceClassName;
  /**
   * A FieldSpec for a field in the generated class that will contain the current activity.
   */
  private final FieldSpec activityField;

  MethodAnnotation(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      TypeName builderType,
      String key,
      String title,
      int layoutId) throws RemixerAnnotationException {
    this.sourceClass = sourceClass;
    this.sourceMethod = sourceMethod;
    this.builderType = builderType;
    this.key = Strings.isNullOrEmpty(key) ? sourceMethod.getSimpleName().toString() : key;
    key = this.key;
    if (!KeyChecker.isValidKey(key)) {
      throw new RemixerAnnotationException(sourceMethod, "Invalid key used, " + key);
    }
    this.title = Strings.isNullOrEmpty(title) ? key : title;
    this.layoutId = layoutId;
    sourceClassName = ClassName.get(sourceClass);
    activityField = FieldSpec.builder(
        sourceClassName, "activity", Modifier.PRIVATE, Modifier.FINAL).build();
    generatedClassName = String.format(Locale.getDefault(), "Generated_%s", key);
    callbackName = key + CALLBACK_NAME_SUFFIX;
    remixerItemName = key + REMIXER_ITEM_SUFFIX;
  }

  public String getKey() {
    return key;
  }

  ExecutableElement getSourceMethod() {
    return sourceMethod;
  }

  private void createBuilder(MethodSpec.Builder methodBuilder) {
    // Create the callback variable.
    methodBuilder.addStatement(
        "$L $L = new $L(activity)", generatedClassName, callbackName, generatedClassName);
    // Create the builder and start filling common things.
    methodBuilder.addStatement("$T $L = new $T()", builderType, remixerItemName, builderType);
    methodBuilder.addStatement("$L.setKey($S)", remixerItemName, key);
    methodBuilder.addStatement("$L.setTitle($S)", remixerItemName, title);
    methodBuilder.addStatement("$L.setLayoutId($L)", remixerItemName, layoutId);
    methodBuilder.addStatement("$L.setContext(activity)", remixerItemName);
    methodBuilder.addStatement("$L.setCallback($L)", remixerItemName, callbackName);
  }

  /**
   * Adds all the code statements necessary to initialize a {@link RemixerItem} that corresponds to
   * the annotation.
   *
   * @param methodBuilder A Method builder has an instance of {@link Remixer} called
   *     {@code remixer} and a context object called {@code activity}. This builder corresponds to a
   *     method in the class that will contain the class generated by
   *     {@link #generateCallbackClass()}.
   */
  final void addSetupStatements(MethodSpec.Builder methodBuilder) {
    // Create the callback variable.
    methodBuilder.addStatement(
        "$L $L = new $L(activity)", generatedClassName, callbackName, generatedClassName);
    // Create the builder and start filling common things.
    methodBuilder.addStatement("$T $L = new $T()", builderType, remixerItemName, builderType);
    methodBuilder.addStatement("$L.setKey($S)", remixerItemName, key);
    methodBuilder.addStatement("$L.setTitle($S)", remixerItemName, title);
    methodBuilder.addStatement("$L.setLayoutId($L)", remixerItemName, layoutId);
    methodBuilder.addStatement("$L.setContext(activity)", remixerItemName);
    methodBuilder.addStatement("$L.setCallback($L)", remixerItemName, callbackName);
    addSpecificSetupStatements(methodBuilder);
    methodBuilder.addStatement("remixer.addItem($L.build())", remixerItemName);
  }

  /**
   * Adds all the statements necessary to initialize the {@link RemixerItem} that are specific to
   * the concrete subclass of {@code RemixerItem} this {@code MethodAnnotation} generates.
   * @param methodBuilder A Method builder has an instance of {@link Remixer} called
   *     {@code remixer} and a context object called {@code activity}. It also has a
   *     {@link com.google.android.libraries.remixer.RemixerItem.Builder} called
   *     {@code remixerItemName} that corresponds to this {@code MethodAnnotation}. This method
   *     builder corresponds to a method in the class that will contain the class generated by
   *     {@link #generateCallbackClass()}.
   */
  protected abstract void addSpecificSetupStatements(MethodSpec.Builder methodBuilder);

  /**
   * Generates a class named {@code generatedClassName} which is an implementation of
   * {@code Callback} that calls the {@code sourceMethod} on the activity.
   */
  TypeSpec generateCallbackClass() {
    MethodSpec method = getCallbackMethodSpec();
    MethodSpec constructor = MethodSpec.constructorBuilder()
        .addParameter(sourceClassName, "activity")
        .addStatement("this.$N = $N", "activity", "activity").build();
    return TypeSpec.classBuilder(generatedClassName)
        .addSuperinterface(getCallbackSuperinterface())
        .addField(activityField)
        .addModifiers(Modifier.STATIC)
        .addMethod(constructor)
        .addMethod(method)
        .build();
  }

  TypeElement getSourceClass() {
    return sourceClass;
  }

  /**
   * Returns the type name for the interface to implement on the callback class.
   */
  protected TypeName getCallbackSuperinterface() {
    return ParameterizedTypeName.get(
        ClassName.get(Callback.class), getVariableType());
  }

  /**
   * Generates the parameter spec for the {@link Callback#onValueSet} implementation
   * generated by this annotation. This parameter is of type {@code Variable<Type>}.
   */
  private ParameterSpec getVariableParameterSpec() {
    return ParameterSpec.builder(
        ParameterizedTypeName.get(ClassName.get(Variable.class), getVariableType()),
        "variable")
        .build();
  }

  /**
   * Generates the method spec for the implementation of {@link Callback#onValueSet}.
   */
  protected MethodSpec getCallbackMethodSpec() {
    return MethodSpec.methodBuilder("onValueSet")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(void.class)
        .addParameter(getVariableParameterSpec())
        .addStatement("activity.$L(variable.getSelectedValue())", sourceMethod.getSimpleName())
        .build();
  }

  /**
   * Returns the type to use to parametrize the Variable objects when generating code.
   *
   * <p>This must be implemented by the subclass depending on the annotation's type.
   */
  protected abstract TypeName getVariableType();
}
