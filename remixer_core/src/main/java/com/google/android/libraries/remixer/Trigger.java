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

import java.util.Locale;

/**
 * A runnable that can be triggered via any of the Remixer interfaces.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class Trigger extends RemixerItem {
  private Runnable runnable;

  public Trigger(String title, String key, Object parentObject, Runnable runnable) {
    this(title, key, parentObject, runnable, 0);
  }

  public Trigger(String title, String key, Object parentObject, Runnable runnable, int layoutId) {
    super(title, key, parentObject, layoutId);
    this.runnable = runnable;
  }

  /**
   * 'Pulls the trigger' and runs the enclosed runnable.
   */
  public void trigger() {
    if (runnable != null) {
      runnable.run();
    }
  }

  @Override
  void clearCallback() {
    runnable = null;
  }

  @Override
  void assertIsCompatibleWith(RemixerItem item) {
    if (item.getKey().equals(getKey()) && item.getClass() != this.getClass()) {
      throw new IncompatibleRemixerItemsWithSameKeyException(
          String.format(
              Locale.getDefault(),
              "%s is incompatible with Trigger with same key %s",
              item.getClass().getCanonicalName(),
              getKey()));
    }
  }
}
