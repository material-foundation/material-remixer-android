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
import com.google.android.libraries.remixer.annotation.IntegerListVariableMethod;
import com.google.android.libraries.remixer.annotation.StringListVariableMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link StringListVariableMethod} and {@link IntegerListVariableMethod}
 * annotations.
 */
class ItemListVariableMethodAnnotation<T> extends MethodAnnotation {

  /**
   * Suffix to add to the name of a list holding the possible values for this variable.
   */
  private static final String LIST_SUFFIX = "_variable_list";

  /**
   * Statement to add a value to the list of possible values.
   */
  private static final String ADD_ITEM = "$L.add($L)";
  /**
   * Statement to add a string value to the list of possible values.
   *
   * <p>The difference lies in treating the value as a string (escaping it in quotes) instead
   * of a literal (variable name or primitive constant).
   */
  private static final String ADD_STRING_ITEM = "$L.add($S)";
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

  static ItemListVariableMethodAnnotation<Integer> forIntegerListVariableMethod(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      IntegerListVariableMethod annotation)
      throws RemixerAnnotationException {
    Integer[] possibleValues = new Integer[annotation.possibleValues().length];
    for (int i = 0; i < annotation.possibleValues().length; i++) {
      possibleValues[i] = annotation.possibleValues()[i];
    }
    return new ItemListVariableMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        annotation.isColor() ? DataType.COLOR : DataType.NUMBER,
        ParameterizedTypeName.get(
            ClassName.get(ItemListVariable.Builder.class), ClassName.get(Integer.class)),
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        possibleValues,
        annotation.defaultValue(),
        0);
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
    String addValueStatement = getAddValueStatement();
    for (T value : possibleValues) {
      methodBuilder.addStatement(addValueStatement, listName, value);
    }
    methodBuilder.addStatement("$L.setPossibleValues($L)", remixerItemName, listName);

    methodBuilder.addStatement(
        defaultValue.getClass().equals(String.class)
            ? "$L.setDefaultValue($S)" : "$L.setDefaultValue($L)",
        remixerItemName, defaultValue);
  }

  private String getAddValueStatement() {
    return defaultValue.getClass().equals(String.class) ? ADD_STRING_ITEM : ADD_ITEM;
  }
}
