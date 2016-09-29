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

package com.google.android.libraries.remixer;

/**
 * This object is a callback for when the value is set.
 */
public interface Callback<T> {

  /**
   * This method will be called when the value is set, even during the initial set up.
   *
   * @param variable The Variable whose value was set, if you need its current value you can call
   *     variable.getSelectedValue().
   */
  void onValueSet(Variable<T> variable);
}
