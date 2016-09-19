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
import com.google.android.libraries.remixer.annotation.StringRemixMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link StringRemixMethod} annotations.
 */
public class StringRemixMethodAnnotation extends MethodAnnotation {

  /**
   * Statement to create a new AnyValueRemix&lt;String&gt;
   *
   * <p>Would expand to {@code AnyValueRemix&lt;String&gt; remixName = new AnyValueRemix&lt;&gt;(
   * title, key, defaultValue, callback, layoutId)}.
   */
  private static final String NEW_STRING_REMIX_STATEMENT = "$T $L = new $T($S, $S, $S, $L, $L)";
  private final String defaultValue;

  public StringRemixMethodAnnotation(
      TypeElement sourceClass, ExecutableElement sourceMethod, StringRemixMethod annotation)
      throws com.google.android.libraries.remixer.annotation.processor.RemixerAnnotationException {
    super(sourceClass, sourceMethod, annotation.key(), annotation.title(), annotation.layoutId());
    defaultValue = annotation.defaultValue();
  }

  @Override
  public void addSetupStatements(MethodSpec.Builder methodBuilder) {
    String callbackVariable = key + CALLBACK_VAR_SUFFIX;
    String remixVariable = key + REMIX_VAR_SUFFIX;
    ParameterizedTypeName remixClass = ParameterizedTypeName.get(
        ClassName.get(Remix.class), ClassName.get(String.class));

    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackVariable, generatedClassName)
        .addStatement(
            NEW_STRING_REMIX_STATEMENT,
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

  @Override
  protected TypeName getRemixType() {
    return ClassName.get(String.class);
  }
}
