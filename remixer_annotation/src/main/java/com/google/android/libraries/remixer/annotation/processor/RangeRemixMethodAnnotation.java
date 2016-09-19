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

import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.annotation.RangeRemixMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link RangeRemixMethod} annotations.
 */
public class RangeRemixMethodAnnotation extends MethodAnnotation {

  /**
   * Statement to create a new RangeRemix.
   *
   * <p>Would expand to {@code RangeRemix remixName = new RangeRemix(title, key,
   * defaultValue, minValue, maxValue, callback, layoutId)}.
   */
  private static final String NEW_RANGE_REMIX_STATEMENT =
      "$T $L = new $T($S, $S, $L, $L, $L, $L, $L, $L)";

  private final int minValue;
  private int defaultValue;
  private final int increment;
  private final int maxValue;

  protected RangeRemixMethodAnnotation(
      TypeElement sourceClass, ExecutableElement sourceMethod, RangeRemixMethod annotation)
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
    String callbackVariable = key + CALLBACK_VAR_SUFFIX;
    String remixVariable = key + REMIX_VAR_SUFFIX;
    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackVariable, generatedClassName)
        .addStatement(
            NEW_RANGE_REMIX_STATEMENT,
            RangeRemix.class,
            remixVariable,
            RangeRemix.class,
            title,
            key,
            defaultValue,
            minValue,
            maxValue,
            increment,
            callbackVariable,
            layoutId)
        .addStatement(INIT_REMIX_STATEMENT, remixVariable)
        .addStatement(ADD_REMIX_STATEMENT, remixVariable);
  }

  @Override
  public TypeName getRemixType() {
    return ClassName.get(Integer.class);
  }
}
