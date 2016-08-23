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

import android.support.annotation.NonNull;

import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * An interface that all remix widgets must implement.
 *
 * <p>This lets the {@link RemixerFragment} bind them to a {@link Remix} when they are inflated.
 */
public interface RemixWidget<T extends Remix<?>> {
  /**
   * Binds the remix to the widget in question.
   *
   * <p>Subclasses need to set up all the callbacks from the UI to set the appropriate value on the
   * backing Remix.
   */
  void bindRemix(@NonNull T remix);
}
