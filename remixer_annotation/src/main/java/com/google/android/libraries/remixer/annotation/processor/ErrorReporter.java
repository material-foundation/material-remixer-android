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

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Utilities class to log from the Annotation processor.
 */
public class ErrorReporter {
  private final Messager messager;

  public ErrorReporter(ProcessingEnvironment env) {
    this.messager = env.getMessager();
  }

  /**
   * Adds a note to the compiler log.
   */
  public void reportNote(Element element, String msg) {
    messager.printMessage(Diagnostic.Kind.NOTE, msg, element);
  }

  /**
   * Adds a warning to the compiler log.
   */
  public void reportWarning(Element element, String msg) {
    messager.printMessage(Diagnostic.Kind.WARNING, msg, element);
  }

  /**
   * Adds an error to the compiler log.
   */
  public void reportError(Element element, String msg) {
    messager.printMessage(Diagnostic.Kind.ERROR, msg, element);
  }
}
