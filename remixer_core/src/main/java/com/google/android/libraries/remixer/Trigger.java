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
 * A runnable that can be triggered via any of the Remixer interfaces.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class Trigger extends RemixerItem {
  private final Runnable runnable;

  public Trigger(String title, String key, Runnable runnable) {
    this(title, key, runnable, 0);
  }

  public Trigger(String title, String key, Runnable runnable, int layoutId) {
    super(title, key, layoutId);
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
}
