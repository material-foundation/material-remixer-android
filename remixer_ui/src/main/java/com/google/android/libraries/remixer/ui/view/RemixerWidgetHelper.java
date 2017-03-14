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

package com.google.android.libraries.remixer.ui.view;

import com.google.android.libraries.remixer.Variable;
import java.util.Locale;

/**
 * Utility class to determine what widget to inflate for a variable.
 */
final class RemixerWidgetHelper {

  private RemixerWidgetHelper() {}

  private static final String UNKNOWN_DEFAULT_ERROR_FORMAT =
      "Variable with key %s, data type %s and class %s has no mapping to a layout resource."
      + " Cannot display it.";

  /**
   * Returns the layout id to inflate for this Variable.
   *
   * <p>If no layout has been specified for the variable (it is 0), it tries to fall back to
   * known default layout ids.
   *
   * @throws IllegalArgumentException if the Variable in question has no default layout associated
   *     with it and it is relying on a default.
   */
  @SuppressWarnings("unchecked")
  static int getLayoutId(Variable instance) {
    int layoutId = instance.getLayoutId();
    if (layoutId != 0) {
      // This instance has a preferred layout.
      return layoutId;
    }
    try {
      return instance.getDataType().getLayoutIdForVariableType(instance.getClass());
    } catch (NullPointerException ex) {
      // There is no mapping, there is no layoutId whatsoever. What do we do? Throw an informative
      // exception.
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              UNKNOWN_DEFAULT_ERROR_FORMAT,
              instance.getKey(), instance.getDataType().getName(), instance.getClass().getName()),
          ex);
    }
  }
}
