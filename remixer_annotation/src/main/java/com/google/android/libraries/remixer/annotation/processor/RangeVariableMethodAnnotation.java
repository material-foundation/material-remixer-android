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

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.common.collect.Range;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link RangeVariableMethod} annotations.
 */
class RangeVariableMethodAnnotation extends MethodAnnotation {

  private final int minValue;
  private int defaultValue;
  private final int increment;
  private final int maxValue;

  RangeVariableMethodAnnotation(
      TypeElement sourceClass, ExecutableElement sourceMethod, RangeVariableMethod annotation)
      throws RemixerAnnotationException {
    super(
        sourceClass,
        sourceMethod,
        DataType.NUMBER,
        ClassName.get(RangeVariable.Builder.class),
        annotation.key(),
        annotation.title(),
        annotation.layoutId());
    minValue = annotation.minValue();
    maxValue = annotation.maxValue();
    increment = annotation.increment();
    defaultValue = annotation.defaultValue();
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
  protected void addSpecificSetupStatements(MethodSpec.Builder methodBuilder) {
    methodBuilder.addStatement("$L.setMinValue($L)", remixerItemName, minValue);
    methodBuilder.addStatement("$L.setMaxValue($L)", remixerItemName, maxValue);
    methodBuilder.addStatement("$L.setDefaultValue($L)", remixerItemName, defaultValue);
    methodBuilder.addStatement("$L.setIncrement($L)", remixerItemName, increment);
  }
}
