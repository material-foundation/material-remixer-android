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
 * Contains all of the Remixer UI Elements that represent an individual Variable in the UI, these
 * are the RemixerWidget family of classes.
 *
 * <p>Each RemixerWidget is bound to a corresponding layout id (i.e.
 * {@link com.google.android.libraries.remixer.ui.widget.BooleanVariableWidget} is instantiated by
 * inflating {@code R.layout.boolean_variable_widget}).
 *
 * <p>All of the Remixer UI elements must implement
 * {@link com.google.android.libraries.remixer.ui.widget.RemixerWidget}, that allows the
 * RemixerWidget to have its corresponding {@link com.google.android.libraries.remixer.Variable}
 * assigned.
 */
package com.google.android.libraries.remixer.ui.widget;
