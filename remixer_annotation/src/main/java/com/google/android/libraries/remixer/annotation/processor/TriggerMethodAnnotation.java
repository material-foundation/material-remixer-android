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

import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.annotation.TriggerMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Generates code to support {@link TriggerMethod} annotations.
 */
class TriggerMethodAnnotation extends MethodAnnotation {

  /**
   * Statement to create a new Trigger.
   *
   * <p>Would expand to {@code Trigger triggerName = new Trigger(title, key, callback, layoutId)}.
   */
  private static final String NEW_TRIGGER_STATEMENT = "$T $L = new $T($S, $S, $L, $L)";
  private static final String TRIGGER_VAR_SUFFIX = "_trigger";

  TriggerMethodAnnotation(
      TypeElement sourceClass, ExecutableElement sourceMethod, TriggerMethod annotation)
      throws RemixerAnnotationException {
    super(sourceClass, sourceMethod, annotation.key(), annotation.title(), annotation.layoutId());
  }

  @Override
  public void addSetupStatements(MethodSpec.Builder methodBuilder) {
    String callbackVariable = key + CALLBACK_VAR_SUFFIX;
    String triggerVariable = key + TRIGGER_VAR_SUFFIX;
    methodBuilder
        .addStatement(
            NEW_CALLBACK_STATEMENT,
            generatedClassName, callbackVariable, generatedClassName)
        .addStatement(
            NEW_TRIGGER_STATEMENT,
            Trigger.class,
            triggerVariable,
            Trigger.class,
            title,
            key,
            callbackVariable,
            layoutId)
        .addStatement(ADD_REMIX_STATEMENT, triggerVariable);
  }

  @Override
  protected TypeName getRemixType() {
    // This does not apply to Triggers, they're basically typeless remixes
    return null;
  }

  @Override
  protected TypeName getCallbackSuperinterface() {
    return ClassName.get(Runnable.class);
  }

  @Override
  protected MethodSpec getCallbackMethodSpec() {
    return MethodSpec.methodBuilder("run")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(void.class)
        .addStatement("activity.$L()", sourceMethod.getSimpleName())
        .build();
  }
}
