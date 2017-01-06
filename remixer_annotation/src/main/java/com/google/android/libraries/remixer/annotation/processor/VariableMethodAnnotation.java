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

import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.StringVariableMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support Remixer Method annotations that generate Variable&lt;T&gt;, such as
 * {@link BooleanVariableMethod} and {@link StringVariableMethod}.
 *
 * <p>While in remixer_core all variables are either of class Variable or of a subclass, these
 * MethodAnnotation classes don't have an equivalent hierarchy (neither
 * RangeVariableMethodAnnotation nor ItemListVariableMethodAnnotation extend this class), since
 * these classes handle mostly the Variable objects' initialization and those are fairly different
 * for each of those classes.
 */
class VariableMethodAnnotation<T> extends MethodAnnotation {

  private final T defaultValue;

  static VariableMethodAnnotation<Boolean> forBooleanVariableMethod(
      TypeElement sourceClass, ExecutableElement sourceMethod, BooleanVariableMethod annotation)
      throws RemixerAnnotationException {
    return new VariableMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        DataType.BOOLEAN,
        ClassName.get(BooleanVariableBuilder.class),
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        annotation.defaultValue());
  }

  static VariableMethodAnnotation<String> forStringVariableMethod(
      TypeElement sourceClass, ExecutableElement sourceMethod, StringVariableMethod annotation)
      throws RemixerAnnotationException {
    return new VariableMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        DataType.STRING,
        ClassName.get(StringVariableBuilder.class),
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        annotation.defaultValue());
  }

  private VariableMethodAnnotation(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      DataType dataType,
      TypeName builderType,
      String key,
      String title,
      int layoutId,
      T defaultValue)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, dataType, builderType, key, title, layoutId);
    this.defaultValue = defaultValue;
  }

  @Override
  protected void addSpecificSetupStatements(MethodSpec.Builder methodBuilder) {
    methodBuilder.addStatement(
        defaultValue.getClass().equals(String.class)
            ? "$L.setDefaultValue($S)" : "$L.setDefaultValue($L)",
        remixerItemName, defaultValue);
  }
}
