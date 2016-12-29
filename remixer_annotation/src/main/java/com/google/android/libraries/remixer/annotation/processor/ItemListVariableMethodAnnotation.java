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
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.annotation.ColorListVariableMethod;
import com.google.android.libraries.remixer.annotation.NumberListVariableMethod;
import com.google.android.libraries.remixer.annotation.StringListVariableMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.Locale;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link StringListVariableMethod} and {@link ColorListVariableMethod}
 * annotations.
 */
class ItemListVariableMethodAnnotation<T> extends MethodAnnotation {

  /**
   * Suffix to add to the name of a list holding the possible values for this variable.
   */
  private static final String LIST_SUFFIX = "_variable_list";

  /**
   * Statement format for String.format to add a value to the list of possible values.
   */
  private static final String ADD_ITEM_FORMAT = "$L.add(%s)";
  /**
   * Statement format for String.format to set the default value.
   */
  private static final String SET_DEFAULT_FORMAT = "$L.setDefaultValue(%s)";
  /**
   * Javapoet format escaping for float values
   */
  private static final String FLOAT_JAVAPOET_ESCAPING = "$Lf";
  /**
   * Javapoet format escaping for integer values
   */
  private static final String INTEGER_JAVAPOET_ESCAPING = "$L";
  /**
   * Javapoet format escaping for string values
   */
  private static final String STRING_JAVAPOET_ESCAPING = "$S";
  /**
   * Statement to create an object through a no-parameter constructor.
   *
   * <p>This is used to create the list of possible values ({@code ArrayList&lt;T&gt; list =
   * new ArrayList&lt;T&gt;()}.
   */
  private static final String CREATE_NEW_OBJECT = "$T $L = new $T()";

  private final T defaultValue;
  private T[] possibleValues;

  static ItemListVariableMethodAnnotation<String> forStringListVariableMethod(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      StringListVariableMethod annotation)
      throws RemixerAnnotationException {
    return new ItemListVariableMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        DataType.STRING,
        ParameterizedTypeName.get(
            ClassName.get(ItemListVariable.Builder.class), ClassName.get(String.class)),
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        annotation.possibleValues(),
        annotation.defaultValue(),
        "");
  }

  static ItemListVariableMethodAnnotation<Integer> forColorListVariableMethod(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      ColorListVariableMethod annotation)
      throws RemixerAnnotationException {
    Integer[] possibleValues = new Integer[annotation.possibleValues().length];
    for (int i = 0; i < annotation.possibleValues().length; i++) {
      possibleValues[i] = annotation.possibleValues()[i];
    }
    return new ItemListVariableMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        DataType.COLOR,
        ParameterizedTypeName.get(
            ClassName.get(ItemListVariable.Builder.class), ClassName.get(Integer.class)),
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        possibleValues,
        annotation.defaultValue(),
        0);
  }

  static ItemListVariableMethodAnnotation<Float> forNumberListVariableMethod(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      NumberListVariableMethod annotation)
      throws RemixerAnnotationException {
    Float[] possibleValues = new Float[annotation.possibleValues().length];
    for (int i = 0; i < annotation.possibleValues().length; i++) {
      possibleValues[i] = annotation.possibleValues()[i];
    }
    return new ItemListVariableMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        DataType.NUMBER,
        ParameterizedTypeName.get(
            ClassName.get(ItemListVariable.Builder.class), ClassName.get(Float.class)),
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        possibleValues,
        annotation.defaultValue(),
        0f);
  }

  /**
   * Constructs an ItemListVariableMethodAnnotation and makes sure that the constraints for these
   * annotations are met.
   *
   * <p>If the default is unset and the zero value (0 for numbers, empty string for Strings) is not
   * amongst the possible values then it falls back to the first value in the list.
   *
   * @throws RemixerAnnotationException if the constraints are not met:
   *     - The list of possible values is empty.
   *     - The default is explicitly set to an unknown value.
   */
  private ItemListVariableMethodAnnotation(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      DataType dataType,
      TypeName builderTypeName,
      String key,
      String title,
      int layoutId,
      T[] possibleValues,
      T defaultValue,
      T zeroValue)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, dataType, builderTypeName, key, title, layoutId);
    this.possibleValues = possibleValues;
    if (possibleValues.length == 0) {
      throw new RemixerAnnotationException(sourceMethod, "List of possible values cannot be empty");
    }
    boolean containsDefault = false;
    for (T s : possibleValues) {
      if (s.equals(defaultValue)) {
        containsDefault = true;
        break;
      }
    }
    if (!containsDefault) {
      if (defaultValue.equals(zeroValue)) {
        // If no default value was set (empty) and the list of possible values doesn't contain the
        // zero-value (empty string in case of string, 0 in case of numbers) then assume the first
        // value in the list to be the default value.
        this.defaultValue = possibleValues[0];
      } else {
        throw new RemixerAnnotationException(sourceMethod,
            "Default value explicitly set to unknown value");
      }
    } else {
      this.defaultValue = defaultValue;
    }
  }

  @Override
  public void addSpecificSetupStatements(MethodSpec.Builder methodBuilder) {
    String listName = key + LIST_SUFFIX;
    TypeName listType =
        ParameterizedTypeName.get(ClassName.get(ArrayList.class),
            ClassName.get(dataType.getValueClass()));
    methodBuilder.addStatement(CREATE_NEW_OBJECT, listType, listName, listType);
    String addValueStatement =
        String.format(Locale.getDefault(), ADD_ITEM_FORMAT, getJavaPoetEscaping());
    for (T value : possibleValues) {
      methodBuilder.addStatement(addValueStatement, listName, value);
    }
    methodBuilder.addStatement("$L.setPossibleValues($L)", remixerItemName, listName);

    String setDefaultValueStatement =
        String.format(Locale.getDefault(), SET_DEFAULT_FORMAT, getJavaPoetEscaping());
    methodBuilder.addStatement(setDefaultValueStatement, remixerItemName, defaultValue);
  }

  private String getJavaPoetEscaping() {
    if (dataType.getName().equals(DataType.STRING.getName()))
      return STRING_JAVAPOET_ESCAPING;
    if (dataType.getName().equals(DataType.COLOR.getName()))
      return INTEGER_JAVAPOET_ESCAPING;
    // assume number
    return FLOAT_JAVAPOET_ESCAPING;
  }
}
