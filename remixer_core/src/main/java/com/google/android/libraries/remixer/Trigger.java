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
  private Runnable runnable;

  private Trigger(String title, String key, Object context, Runnable runnable, int layoutId) {
    super(title, key, context, layoutId);
    this.runnable = runnable;
  }

  /**
   * 'Pulls the trigger' and runs the enclosed runnable. This method also triggers all other
   * triggers (in other contexts) with the same key.
   */
  public void trigger() {
    triggerWithoutTriggeringOthers();
    triggerOthersWithTheSameKey();
  }

  /**
   * 'Pulls the trigger' and runs the enclosed runnable without triggering other triggers.
   *
   * <b>Internal use only. Users should never call this method.</b>
   */
  public void triggerWithoutTriggeringOthers() {
    if (runnable != null) {
      runnable.run();
    }
  }

  /**
   * Triggers all other triggers with the same key.
   */
  private void triggerOthersWithTheSameKey() {
    if (remixer == null) {
      // This instance hasn't been added to a Remixer, probably still being set up, abort.
      return;
    }
    remixer.onTrigger(this);
  }

  public static class Builder extends RemixerItem.Builder<Trigger, Runnable> {

    @Override
    public Trigger build() {
      checkBaseFields();
      return new Trigger(title, key, context, callback, layoutId);
    }
  }
}
