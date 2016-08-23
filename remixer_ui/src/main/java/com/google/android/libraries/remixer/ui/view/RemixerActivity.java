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

package com.google.android.libraries.remixer.ui.view;

import com.google.android.libraries.remixer.Remixer;

/**
 * Interface that all activities that use Remixer must implement.
 *
 * <p>This interface allows {@link RemixerFragment} to interact with the host activity and set
 * itself up with the Activity's {@link Remixer}.
 */
public interface RemixerActivity {
  Remixer getRemixer();
}
