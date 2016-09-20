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

import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.annotation.BooleanRemixMethod;
import com.google.android.libraries.remixer.annotation.StringRemixMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support RemixMethod annotations that generate Remix&lt;T&gt;, such as
 * {@link BooleanRemixMethod} and {@link StringRemixMethod}.
 *
 * <p>While in remixer_core all remixes are either of class Remix or of a subclass, these
 * MethodAnnotation classes don't have an equivalent hierarchy (neither RangeRemixMethodAnnotation
 * nor ItemListRemixMethodAnnotation extend this class), since these classes handle mostly
 * the Remix objects' initialization and those are fairly different for each of those classes.
 *
 * <p>Most of what can be shared is shared in MethodAnnotation and also applies to @TriggerMethod
 * annotations as well.
 */
class RemixMethodAnnotation<T> extends MethodAnnotation {

  /**
   * Statement to create a new Remix&lt;T&gt;
   *
   * <p>Would expand to {@code Remix&lt;T&gt; remixName = new Remix&lt;&gt;(
   * title, key, defaultValue, callback, layoutId)}.
   */
  private static final String NEW_REMIX_STATEMENT =
      "$T $L = new $T($S, $S, $L, $L, $L)";

  /**
   * Statement to create a new Remix&lt;String&gt;
   *
   * <p>Would expand to {@code Remix&lt;String&gt; remixName = new Remix&lt;&gt;(
   * title, key, "defaultValue", callback, layoutId)}.
   *
   * <p>The difference lays in that the defaultValue is treated as a string and quoted, instead of
   * being treated like a literal
   */
  private static final String NEW_STRING_REMIX_STATEMENT =
      "$T $L = new $T($S, $S, $S, $L, $L)";

  private final T defaultValue;

  static RemixMethodAnnotation<Boolean> forBooleanRemixMethod(
      TypeElement sourceClass, ExecutableElement sourceMethod, BooleanRemixMethod annotation)
      throws RemixerAnnotationException {
    return new RemixMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        annotation.defaultValue());
  }

  static RemixMethodAnnotation<String> forStringRemixMethod(
      TypeElement sourceClass, ExecutableElement sourceMethod, StringRemixMethod annotation)
      throws RemixerAnnotationException {
    return new RemixMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        annotation.defaultValue());
  }

  private RemixMethodAnnotation(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      String key,
      String title,
      int layoutId,
      T defaultValue)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, key, title, layoutId);
    this.defaultValue = defaultValue;
  }

  @Override
  public void addSetupStatements(MethodSpec.Builder methodBuilder) {
    String callbackVariable = key + CALLBACK_VAR_SUFFIX;
    String remixVariable = key + REMIX_VAR_SUFFIX;
    ParameterizedTypeName remixClass = ParameterizedTypeName.get(
        ClassName.get(Remix.class), getRemixType());
    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackVariable, generatedClassName)
        .addStatement(
            getNewRemixStatement(),
            remixClass,
            remixVariable,
            remixClass,
            title,
            key,
            defaultValue,
            callbackVariable,
            layoutId)
        .addStatement(INIT_REMIX_STATEMENT, remixVariable)
        .addStatement(ADD_REMIX_STATEMENT, remixVariable);
  }

  private String getNewRemixStatement() {
    return defaultValue.getClass().equals(String.class)
        ? NEW_STRING_REMIX_STATEMENT : NEW_REMIX_STATEMENT;
  }

  @Override
  protected TypeName getRemixType() {
    return ClassName.get(defaultValue.getClass());
  }
}
