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

import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.annotation.IntegerListRemixMethod;
import com.google.android.libraries.remixer.annotation.StringListRemixMethod;
import com.google.common.base.Strings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link StringListRemixMethod} annotations.
 */
class ItemListRemixMethodAnnotation<T> extends MethodAnnotation {

  /**
   * Suffix to add to the variable name of a list holding the possible values for this remix.
   */
  private static final String REMIX_LIST_SUFFIX = "_remix_list";

  /**
   * Statement to create a new ItemListRemix&lt;T&gt;.
   *
   * <p>Would expand to {@code StringListRemix remixName = new StringListRemix(title, key,
   * defaultValue, possibleValues, callback, layoutId)}.
   */
  private static final String NEW_ITEM_LIST_REMIX_STATEMENT =
      "$T $L = new $T($S, $S, $L, $L, $L, $L)";

  /**
   * Statement to create a new StringListRemix.
   *
   * <p>Would expand to {@code StringListRemix remixName = new StringListRemix(title, key,
   * "defaultValue", possibleValues, callback, layoutId)}.
   *
   * <p>The difference lies in treating the defaultValue as a string (escaping it in quotes) instead
   * of a literal (variable name or primitive constant).
   */
  private static final String NEW_STRING_LIST_REMIX_STATEMENT =
      "$T $L = new $T($S, $S, $S, $L, $L, $L)";
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

  static ItemListRemixMethodAnnotation<String> forStringListRemixMethod(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      StringListRemixMethod annotation)
      throws RemixerAnnotationException {
    return new ItemListRemixMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        annotation.possibleValues(),
        annotation.defaultValue(),
        "");
  }

  static ItemListRemixMethodAnnotation<Integer> forIntegerListRemixMethod(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      IntegerListRemixMethod annotation)
      throws RemixerAnnotationException {
    Integer[] possibleValues = new Integer[annotation.possibleValues().length];
    for (int i = 0; i < annotation.possibleValues().length; i++) {
      possibleValues[i] = annotation.possibleValues()[i];
    }
    return new ItemListRemixMethodAnnotation<>(
        sourceClass,
        sourceMethod,
        annotation.key(),
        annotation.title(),
        annotation.layoutId(),
        possibleValues,
        annotation.defaultValue(),
        0);
  }

  /**
   * Constructs an ItemListRemixMethodAnnotation and makes sure that the constraints for these
   * annotations are met.
   *
   * <p>If the default is unset and the zero value (0 for numbers, empty string for Strings) is not
   * amongst the possible values then it falls back to the first value in the list.
   *
   * @throws RemixerAnnotationException if the constraints are not met:
   *     - The list of possible values is empty.
   *     - The default is explicitly set to an unknown value.
   */
  private ItemListRemixMethodAnnotation(
      TypeElement sourceClass,
      ExecutableElement sourceMethod,
      String key,
      String title,
      int layoutId,
      T[] possibleValues,
      T defaultValue,
      T zeroValue)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, key, title, layoutId);
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
  public void addSetupStatements(MethodSpec.Builder methodBuilder) {
    String callbackVariable = key + CALLBACK_VAR_SUFFIX;
    String remixVariable = key + REMIX_VAR_SUFFIX;
    String listVariable = key + REMIX_LIST_SUFFIX;
    TypeName stringListType =
        ParameterizedTypeName.get(ClassName.get(ArrayList.class), getRemixType());
    TypeName remixType =
        ParameterizedTypeName.get(ClassName.get(ItemListRemix.class), getRemixType());
    methodBuilder.addStatement(CREATE_NEW_OBJECT, stringListType, listVariable, stringListType);
    String addValueStatement = getAddValueStatement();
    for (T value : possibleValues) {
      methodBuilder.addStatement(addValueStatement, listVariable, value);
    }

    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackVariable, generatedClassName)
        .addStatement(
            getNewRemixStatement(),
            remixType,
            remixVariable,
            remixType,
            title,
            key,
            defaultValue,
            listVariable,
            callbackVariable,
            layoutId)
        .addStatement(INIT_REMIX_STATEMENT, remixVariable)
        .addStatement(ADD_REMIX_STATEMENT, remixVariable);
  }

  @Override
  protected TypeName getRemixType() {
    return ClassName.get(defaultValue.getClass());
  }

  private String getNewRemixStatement() {
    return defaultValue.getClass().equals(String.class)
        ? NEW_STRING_LIST_REMIX_STATEMENT : NEW_ITEM_LIST_REMIX_STATEMENT;
  }

  private String getAddValueStatement() {
    return defaultValue.getClass().equals(String.class) ? ADD_STRING_ITEM : ADD_ITEM;
  }
}
