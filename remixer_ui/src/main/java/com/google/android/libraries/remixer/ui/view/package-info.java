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

/**
 * Contains the code to display the
 * {@link com.google.android.libraries.remixer.ui.view.RemixerFragment}.
 *
 * <p>You should only need to instantiate it and push it to the FragmentManager from an activity
 * that implements {@link com.google.android.libraries.remixer.ui.view.RemixerActivity}:
 *
 * <pre><code>
 * import RemixerActivity;
 * import RemixerFragment;
 * import com.google.android.remixer.Remixer;
 *
 * class MyActivity implements RemixerActivity {
 *   Remixer remixer;
 *   RemixerFragment remixerFragment;
 *
 *   &#064;Override
 *   public Remixer getRemixer() {
 *     return remixer;
 *   }
 *
 *   // ...
 *
 *   void showRemixer() {
 *     if (remixerFragment == null) {
 *       remixerFragment = RemixerFragment.newInstance();
 *     }
 *     remixerFragment.show(getSupportFragmentManager(), "FragmentKey");
 *   }
 * }
 *
 * </code></pre>
 */
package com.google.android.libraries.remixer.ui.view;
