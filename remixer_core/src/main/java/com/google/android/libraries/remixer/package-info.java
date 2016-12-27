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
 * The main remixer package contains all of the logic for Remixes and a main point of entry class,
 * {@link com.google.android.libraries.remixer.Remixer}, which contains all of the active Variables.
 *
 * <p>Most of the logic is implemented by the base class
 * {@link com.google.android.libraries.remixer.Variable}. Subclasses are tasked with checking
 * whether values are valid or not and providing a LayoutRes to display it on the Remixer UI.
 *
 * <b>None of the Variable classes are thread-safe and should only be used from the UI Thread. This
 * way the callbacks are guaranteed to be run on the UI thread.</b>
 */
package com.google.android.libraries.remixer;
