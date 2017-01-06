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

import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.ColorListVariableMethod;
import com.google.android.libraries.remixer.annotation.NumberListVariableMethod;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.StringListVariableMethod;
import com.google.android.libraries.remixer.annotation.StringVariableMethod;
import java.lang.annotation.Annotation;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * An enum of all the supported method annotations.
 */
enum SupportedMethodAnnotation {

  BOOLEAN_VARIABLE(BooleanVariableMethod.class, Boolean.class) {
    @Override
    public MethodAnnotation getMethodAnnotation(
        TypeElement clazz, ExecutableElement method, Annotation baseAnnotation)
        throws RemixerAnnotationException {
      BooleanVariableMethod annotation = (BooleanVariableMethod) baseAnnotation;
      return VariableMethodAnnotation.forBooleanVariableMethod(clazz, method, annotation);
    }
  },
  COLOR_LIST_VARIABLE(ColorListVariableMethod.class, Integer.class) {
    @Override
    public MethodAnnotation getMethodAnnotation(
        TypeElement clazz, ExecutableElement method, Annotation baseAnnotation)
        throws RemixerAnnotationException {
      ColorListVariableMethod annotation = (ColorListVariableMethod) baseAnnotation;
      return
          ItemListVariableMethodAnnotation.forColorListVariableMethod(clazz, method, annotation);
    }
  },
  NUMBER_LIST_VARIABLE(NumberListVariableMethod.class, Float.class) {
    @Override
    public MethodAnnotation getMethodAnnotation(
        TypeElement clazz, ExecutableElement method, Annotation baseAnnotation)
        throws RemixerAnnotationException {
      NumberListVariableMethod annotation = (NumberListVariableMethod) baseAnnotation;
      return
          ItemListVariableMethodAnnotation.forNumberListVariableMethod(clazz, method, annotation);
    }
  },
  RANGE_VARIABLE(RangeVariableMethod.class, Float.class) {
    @Override
    public MethodAnnotation getMethodAnnotation(
        TypeElement clazz, ExecutableElement method, Annotation baseAnnotation)
        throws RemixerAnnotationException {
      RangeVariableMethod annotation = (RangeVariableMethod) baseAnnotation;
      return new RangeVariableMethodAnnotation(clazz, method, annotation);
    }
  },
  STRING_LIST_VARIABLE(StringListVariableMethod.class, String.class) {
    @Override
    public MethodAnnotation getMethodAnnotation(
        TypeElement clazz, ExecutableElement method, Annotation baseAnnotation)
        throws RemixerAnnotationException {
      StringListVariableMethod annotation = (StringListVariableMethod) baseAnnotation;
      return
          ItemListVariableMethodAnnotation.forStringListVariableMethod(clazz, method, annotation);
    }
  },
  STRING_VARIABLE(StringVariableMethod.class, String.class) {
    @Override
    public MethodAnnotation getMethodAnnotation(
        TypeElement clazz, ExecutableElement method, Annotation baseAnnotation)
        throws RemixerAnnotationException {
      StringVariableMethod annotation = (StringVariableMethod) baseAnnotation;
      return VariableMethodAnnotation.forStringVariableMethod(clazz, method, annotation);
    }
  };

  private Class<? extends Annotation> annotationType;
  private Class<?> parameterClass;

  SupportedMethodAnnotation(Class<? extends Annotation> annotationType, Class<?> parameterClass) {
    this.annotationType = annotationType;
    this.parameterClass = parameterClass;
  }

  /**
   * The class object for the annotation.
   */
  public Class<? extends Annotation> getAnnotationType() {
    return annotationType;
  }

  /**
   * The class of the values passed to callback methods for this annotation.
   */
  public Class<?> getParameterClass() {
    return parameterClass;
  }

  /**
   * Returns a {@link MethodAnnotation} object that represents one single MethodAnnotation found in
   * the code. This is used later to generate the code.
   *
   * @throws RemixerAnnotationException Any semantic error or usage of an unimplemented annotation.
   */
  public abstract MethodAnnotation getMethodAnnotation(
      TypeElement clazz, ExecutableElement method, Annotation annotation)
      throws RemixerAnnotationException;
}
