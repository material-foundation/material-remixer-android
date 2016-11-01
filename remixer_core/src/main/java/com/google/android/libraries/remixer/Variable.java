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
 * Base class for all Remixes that does not do any value checking. A variable takes care of calling
 * a callback when the value is changed. It does not support any sort of null values.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class Variable<T> extends RemixerItem {

  /**
   * The callback to be executed when the value is updated.
   */
  private Callback<T> callback;

  /**
   * Creates a new Variable.
   *
   * @param key The key to use to save to SharedPreferences. This needs to be unique across all
   *     Remixes.
   * @param title The name to display in the UI.
   * @param defaultValue The default value for this Variable.
   * @param parentObject the object which created this variable, should be an activity.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param layoutId A layout to inflate when displaying this Variable in the UI.
   */
  // TODO(miguely): Add default value semantics to the defaultValue, currently it behaves mostly
  // as an initial value. It should be used in cases when the value is set to an invalid value from
  // SharedPreferences or Firebase.
  public Variable(
      String title,
      String key,
      T defaultValue,
      Object parentObject,
      Callback<T> callback,
      int layoutId) {
    super(title, key, parentObject, layoutId);
    // TODO(miguely): pull this out of SharedPreferences.
    this.selectedValue = defaultValue;
    this.callback = callback;
  }

  /**
   * Makes sure the default value is valid for this variable and runs the callback if so. This must
   * be called as soon as the Variable is created.
   *
   * @throws IllegalArgumentException The currently selected value (or default value) is invalid for
   *     this Variable. See {@link #checkValue(Object)}.
   */
  public final void init() {
    checkValue(selectedValue);
    runCallback();
  }

  /**
   * The currently selected value.
   */
  private T selectedValue;

  public T getSelectedValue() {
    return selectedValue;
  }

  public Class getVariableType() {
    return selectedValue.getClass();
  }

  /**
   * Checks that the value passed in is a valid value, otherwise throws {@link
   * IllegalArgumentException}.
   *
   * @throws IllegalArgumentException An invalid value was passed in.
   */
  protected void checkValue(T value) {
    // No need to check anything in the base class.
  }

  /**
   * Sets the selected value to a new value.
   *
   * <p>This also notifies all other variables with the same key that the value has changed.
   *
   * @param newValue Value to set. Cannot be null.
   * @throws IllegalArgumentException {@code newValue} is an invalid value for this Variable.
   */
  public void setValue(T newValue) {
    setValueWithoutNotifyingOthers(newValue);
    setValueOnOthersWithTheSameKey();
  }

  /**
   * Sets the selected value to a new value without notifying other variables of this change.
   * <b>Only for internal use!!</b>
   *
   * @param newValue Value to set. Cannot be null.
   * @throws IllegalArgumentException {@code newValue} is an invalid value for this Variable.
   */
  public void setValueWithoutNotifyingOthers(T newValue) {
    checkValue(newValue);
    selectedValue = newValue;
    runCallback();
  }

  /**
   * Sets the new value on all other Variables of the same key.
   */
  @SuppressWarnings("unchecked")
  private void setValueOnOthersWithTheSameKey() {
    if (remixer == null) {
      // This instance hasn't been added to a Remixer, probably still being set up, abort.
      return;
    }
    remixer.onValueChanged(this);
  }

  private void runCallback() {
    if (callback != null) {
      callback.onValueSet(this);
    }
  }

  /**
   * Convenience builder for Variable.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the layout id is not set, the default layout will be used.
   * <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key is mandatory. If it's missing, an {@link IllegalArgumentException}
   * will be thrown.
   */
  public static class Builder<T> {

    private String key;
    private String title;
    private T defaultValue;
    private Object parentObject;
    private Callback<T> callback;
    private int layoutId = 0;

    public Builder() {}

    public Builder<T> setKey(String key) {
      this.key = key;
      return this;
    }

    public Builder<T> setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder<T> setDefaultValue(T defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder<T> setParentObject(Object parentObject) {
      this.parentObject = parentObject;
      return this;
    }

    public Builder<T> setCallback(Callback<T> callback) {
      this.callback = callback;
      return this;
    }

    public Builder<T> setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new Variable created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key is missing
     */
    public Variable<T> buildAndInit() {
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for Variable");
      }
      if (parentObject == null) {
        throw new IllegalArgumentException("parentObject cannot be unset for Variable");
      }
      if (title == null) {
        title = key;
      }
      Variable<T> variable =
          new Variable<T>(title, key, defaultValue, parentObject, callback, layoutId);
      variable.init();
      return variable;
    }
  }
}
