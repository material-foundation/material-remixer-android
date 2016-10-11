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
 * <p>You should only need to instantiate it and push it to the FragmentManager, but RemixerFragment
 * has two convenience methods to set it up:
 * <ul>
 * <li>{@link com.google.android.libraries.remixer.ui.view.RemixerFragment#attachToGesture(
 *     android.support.v4.app.FragmentActivity,
 *     com.google.android.libraries.remixer.ui.gesture.Direction, int)} which sets a gesture to
 *     show the fragment, and...
 * <li>{@link com.google.android.libraries.remixer.ui.view.RemixerFragment#
 *     attachToButton(android.support.v4.app.FragmentActivity, android.widget.Button)} which shows
 *     the fragment on a button click.
 * </ul>
 */
package com.google.android.libraries.remixer.ui.view;
