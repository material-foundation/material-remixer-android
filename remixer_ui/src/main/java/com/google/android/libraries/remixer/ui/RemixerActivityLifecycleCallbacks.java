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

package com.google.android.libraries.remixer.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.google.android.libraries.remixer.Remixer;

/**
 * Listens to activity lifecycle events to be able to clean up remixer when activities are
 * destroyed. This way we avoid leaks.
 */
final class RemixerActivityLifecycleCallbacks
    implements Application.ActivityLifecycleCallbacks {

  private static RemixerActivityLifecycleCallbacks instance =
      new RemixerActivityLifecycleCallbacks();

  private RemixerActivityLifecycleCallbacks() {}


  static RemixerActivityLifecycleCallbacks getInstance() {
    return instance;
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle bundle) {

  }

  @Override
  public void onActivityStarted(Activity activity) {

  }

  @Override
  public void onActivityResumed(Activity activity) {
    Remixer.getInstance().getSynchronizationMechanism().onContextChanged(activity);
  }

  @Override
  public void onActivityPaused(Activity activity) {
    Remixer.getInstance().getSynchronizationMechanism().onContextRemoved(activity);
  }

  @Override
  public void onActivityStopped(Activity activity) {
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

  }

  @Override
  public void onActivityDestroyed(Activity activity) {
    Remixer.getInstance().onActivityDestroyed(activity);
  }
}
