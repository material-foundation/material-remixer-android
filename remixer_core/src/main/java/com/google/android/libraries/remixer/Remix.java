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
 * Base class for all Remix controls. This class implements calling the callback when necessary,
 * value checking, etc.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public abstract class Remix<T> {

  /**
   * The name to display in the UI for this remix.
   */
  private final String title;
  /**
   * The key to use to save to SharedPreferences. This needs to be unique across all remixes.
   */
  private final String key;
  /**
   * The callback to be executed when the value is updated.
   */
  private final RemixCallback callback;
  /**
   * The layout to inflate to display this remix. If set to 0, the default layout associated with
   * the remix type will be used.
   */
  private final int controlViewResourceId;
  /**
   * The currently selected value.
   */
  private T selectedValue;

  /**
   * Creates a new Remix.
   *
   * @param key The key to use to save to SharedPreferences. This needs to be unique across all
   *     Remixes.
   * @param title The name to display in the UI.
   * @param defaultValue The default value for this Remix.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param controlViewResourceId A layout to inflate when displaying this Remix in the UI.
   * @throws IllegalArgumentException defaultValue is invalid for this Remix. See {@link
   *     #checkValue(Object)}.
   */
  // TODO(miguely): Add default value semantics to the defaultValue, currently it behaves mostly
  // as an initial value. It should be used in cases when the value is set to an invalid value from
  // SharedPreferences or Firebase.
  protected Remix(
      String title,
      String key,
      T defaultValue,
      RemixCallback callback,
      int controlViewResourceId) {
    this.key = key;
    this.title = title;
    // TODO(miguely): pull this out of SharedPreferences.
    this.selectedValue = defaultValue;
    this.callback = callback;
    this.controlViewResourceId = controlViewResourceId;
  }

  public String getTitle() {
    return title;
  }

  public String getKey() {
    return key;
  }

  public T getSelectedValue() {
    return selectedValue;
  }

  /**
   * Checks that the value passed in is a valid value, otherwise throws {@link
   * IllegalArgumentException}.
   *
   * @throws IllegalArgumentException An invalid value was passed in.
   */
  protected abstract void checkValue(T value);

  /**
   * Sets the selected value to a new value.
   *
   * <p>This needs to be implemented in each of the remixes that extend this class, it should throw
   * an IllegalArgumentException if the value is invalid.
   *
   * @param newValue Value to set.
   * @throws IllegalArgumentException {@code newValue} is an invalid value for this Remix.
   */
  public void setValue(T newValue) {
    checkValue(newValue);
    selectedValue = newValue;
    runCallback();
  }

  /**
   * Returns the layout id to inflate when displaying this Remix.
   */
  public int getControlViewResourceId() {
    return controlViewResourceId;
  }

  protected void runCallback() {
    if (callback != null) {
      callback.onValueSet(this);
    }
  }
}
