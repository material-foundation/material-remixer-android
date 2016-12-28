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

package com.google.android.libraries.remixer.ui.widget;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * An interface that all remixer widgets must implement.
 *
 * <p>This lets the {@link RemixerFragment} bind them to a {@link Variable} when they are
 * inflated.
 */
@Keep
public interface RemixerWidget<T extends Variable> {
  /**
   * Binds the variable item to the widget in question.
   *
   * <p>Subclasses need to set up all the callbacks from the UI to set the appropriate value on the
   * backing Variable.
   */
  void bindVariable(@NonNull T variable);
}
