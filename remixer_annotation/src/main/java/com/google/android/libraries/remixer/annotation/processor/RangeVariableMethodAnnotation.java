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

import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link RangeVariableMethod} annotations.
 */
class RangeVariableMethodAnnotation extends MethodAnnotation {

  /**
   * Statement to create a new RangeVariable.
   *
   * <p>Would expand to {@code RangeVariable variableName = new RangeVariable(title, key,
   * defaultValue, minValue, maxValue, activity, callback, layoutId)}.
   */
  private static final String NEW_RANGE_VARIABLE_STATEMENT =
      "$T $L = new $T($S, $S, $L, $L, $L, $L, $L, $L, $L)";

  private final int minValue;
  private int defaultValue;
  private final int increment;
  private final int maxValue;

  RangeVariableMethodAnnotation(
      TypeElement sourceClass, ExecutableElement sourceMethod, RangeVariableMethod annotation)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, annotation.key(), annotation.title(), annotation.layoutId());
    minValue = annotation.minValue();
    maxValue = annotation.maxValue();
    increment = annotation.increment();
    if (minValue > maxValue) {
      throw new RemixerAnnotationException(sourceMethod,
          "minValue cannot be greater than maxValue");
    }
    if (minValue > annotation.defaultValue() || maxValue < annotation.defaultValue()) {
      if (annotation.defaultValue() == 0) {
        defaultValue = minValue;
      } else {
        throw new RemixerAnnotationException(sourceMethod,
            "defaultValue was explicitly set out of bounds.");
      }
    }
  }

  @Override
  public void addSetupStatements(MethodSpec.Builder methodBuilder) {
    String callbackName = key + CALLBACK_NAME_SUFFIX;
    String variableName = key + VARIABLE_SUFFIX;
    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackName, generatedClassName)
        .addStatement(
            NEW_RANGE_VARIABLE_STATEMENT,
            RangeVariable.class,
            variableName,
            RangeVariable.class,
            title,
            key,
            defaultValue,
            minValue,
            maxValue,
            increment,
            ACTIVITY_NAME,
            callbackName,
            layoutId)
        .addStatement(INIT_VARIABLE_STATEMENT, variableName)
        .addStatement(ADD_VARIABLE_STATEMENT, variableName);
  }

  @Override
  public TypeName getVariableType() {
    return ClassName.get(Integer.class);
  }
}
