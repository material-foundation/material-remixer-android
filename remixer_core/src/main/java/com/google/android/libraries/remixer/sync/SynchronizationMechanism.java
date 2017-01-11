/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.sync;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.Variable;

/**
 * {@code SynchronizationMechanism}s are the source of truth for values and configuration of
 * RemixerItems in Remixer.
 *
 * <p>The {@link Remixer} object depends on it to keep track of the correct values and make sure new
 * instances always have the newest value.
 *
 * <p>It is not expected that Remixer will take place of the source-control system, so
 * SynchronizationMechanisms should decide on a source of truth and not worry about versioning.
 */
public interface SynchronizationMechanism {

  /**
   * Sets the Remixer instance to use. Normally a project will only have one of those, but this
   * is important for testing, too.
   */
  void setRemixerInstance(Remixer remixer);

  /**
   * Method called right before adding a Variable to Remixer.
   *
   * <p>This allows the SynchronizationMechanism to cache/save/sync the value or override it with a
   * newer value if necessary.
   */
  void onAddingVariable(Variable variable);

  /**
   * Called whenever the value is changed for a variable.
   *
   * <p>The SynchronizationMechanism must alert other instances of the variable of the change and
   * cache/save/sync the new value.
   */
  void onValueChanged(Variable variable);

  /**
   * Called when a new activity has been made the current context.
   *
   * <p>This may be needed to update context-dependent state.
   */
  void onContextChanged(Object currentContext);

  /**
   * Called when an activity is stopped and reclaimed, the context is no longer valid.
   *
   * <p>This may be needed to update context-dependent state. Notice this may be called for a
   * context which is no longer the current context (the one passed in the last call to
   * {@link #onContextChanged(Object)}); it is up to the implementation what to do in this case.
   */
  void onContextRemoved(Object currentContext);
}
