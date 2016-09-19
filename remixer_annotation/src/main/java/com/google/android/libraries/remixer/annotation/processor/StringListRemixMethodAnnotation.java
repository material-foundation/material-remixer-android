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
public class StringListRemixMethodAnnotation extends MethodAnnotation {

  /**
   * Suffix to add to the variable name of a list holding the possible values for this remix.
   */
  public static final String REMIX_LIST_SUFFIX = "_remix_list";

  /**
   * Statement to create a new StringListRemix.
   *
   * <p>Would expand to {@code StringListRemix remixName = new StringListRemix(title, key,
   * defaultValue, possibleValues, callback, layoutId)}.
   */
  private static final String NEW_STRING_LIST_REMIX_STATEMENT =
      "$T $L = new $T($S, $S, $S, $L, $L, $L)";
  private final String defaultValue;
  private String[] possibleValues;

  /**
   * Constructs a StringListRemixMethodAnnotation and makes sure that the constraints for these
   * annotations are met.
   *
   * <p>If the default is unset (empty string) and the empty string is not amongst the possible
   * values then it falls back to the first value in the list of possible values.
   *
   * @throws RemixerAnnotationException if the constraints are not met:
   *     - The list of possible values is empty.
   *     - The default is explicitly set to an unknown value.
   */
  public StringListRemixMethodAnnotation(
      TypeElement sourceClass, ExecutableElement sourceMethod, StringListRemixMethod annotation)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, annotation.key(), annotation.title(), annotation.layoutId());
    possibleValues = annotation.possibleValues();
    if (possibleValues.length == 0) {
      throw new RemixerAnnotationException(sourceMethod, "List of possible values cannot be empty");
    }
    boolean containsDefault = false;
    for (String s : possibleValues) {
      if (s.equals(annotation.defaultValue())) {
        containsDefault = true;
        break;
      }
    }
    if (!containsDefault) {
      if (Strings.isNullOrEmpty(annotation.defaultValue())) {
        // If no default value was set (empty) and the list of possible values doesn't contain the
        // empty string then assume the first value in the list to be the default value.
        defaultValue = possibleValues[0];
      } else {
        throw new RemixerAnnotationException(sourceMethod,
            "Default value explicitly set to unknown value");
      }
    } else {
      defaultValue = annotation.defaultValue();
    }
  }

  @Override
  public void addSetupStatements(MethodSpec.Builder methodBuilder) {
    String callbackVariable = key + CALLBACK_VAR_SUFFIX;
    String remixVariable = key + REMIX_VAR_SUFFIX;
    String listVariable = key + REMIX_LIST_SUFFIX;
    TypeName stringListType =
        ParameterizedTypeName.get(ClassName.get(ArrayList.class), ClassName.get(String.class));
    TypeName remixType =
        ParameterizedTypeName.get(ClassName.get(ItemListRemix.class), ClassName.get(String.class));
    methodBuilder.addStatement("$T $L = new $T()", stringListType, listVariable, stringListType);
    for (String s : possibleValues) {
      methodBuilder.addStatement("$L.add($S)", listVariable, s);
    }

    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackVariable, generatedClassName)
        .addStatement(
            NEW_STRING_LIST_REMIX_STATEMENT,
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
    return ClassName.get(String.class);
  }
}
