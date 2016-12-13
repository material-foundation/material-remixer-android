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
import com.google.android.libraries.remixer.ui.RemixerInitialization;

/**
 * Remixer requires {@link RemixerInitialization#initRemixer(Application)} to be called
 * from the Application class' {@link Application#onCreate()}. This sets up the default data types
 * and corresponding widgets and makes sure leaks are prevented by using
 * RemixerActivityLifecycleCallbacks.
 *
 * <p>This is also where you can set a synchronization mechanism other than the default.
 */
public class RemixerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RemixerInitialization.initRemixer(this);
  }
}
