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

package com.google.android.apps.remixer;

import android.app.Application;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.storage.LocalValueSyncing;
import com.google.android.libraries.remixer.ui.RemixerActivityLifecycleCallbacks;

/**
 * The Remixer Application sets up Remixer.
 *
 * <b>There are two things that must be initialized:
 * <ul>
 *   <li>Register an instance of RemixerActivityLifecycleCallbacks for lifecycle callbacks, this
 *   ensures no leaks happen since it removes all RemixerItems associated with an activity when the
 *   latter is destroyed</li>
 *   <li>Sets the synchronization mechanism for Remixer, this tells Remixer what logic to use to
 *   keep values in sync. In this example we're using simple local-only syncing with no
 *   persistence.</li>
 * </ul>
 */
public class RemixerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    registerActivityLifecycleCallbacks(RemixerActivityLifecycleCallbacks.getInstance());
    Remixer.getInstance().setSynchronizationMechanism(new LocalValueSyncing());
  }
}
