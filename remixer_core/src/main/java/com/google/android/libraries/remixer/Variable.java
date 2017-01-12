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

import com.google.android.libraries.remixer.serialization.StoredVariable;
import java.lang.ref.WeakReference;

/**
 * Base class for all Remixes that does not do any value checking. A variable takes care of calling
 * a callback when the value is changed. It does not support any sort of null values.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class Variable<T> {

  /**
   * The name to display in the UI for this variable.
   */
  private final String title;
  /**
   * The key to use to identify this item across storage and all the interfaces.
   */
  private final String key;
  /**
   * The layout to inflate to display this variable. If set to 0, the default layout associated
   * with the variable type will be used.
   */
  private final int layoutId;
  /**
   * A weak reference to this RemixerItem's context. The RemixerItem's lifecycle is tied to its
   * contexts'.
   *
   * <p>It should be a reference to an activity, but it isn't since remixer_core cannot depend on
   * Android classes. It is a weak reference in order not to leak the activity accidentally.
   */
  private final WeakReference<Object> context;
  /**
   * The remixer instance this RemixerItem has been attached to.
   */
  protected Remixer remixer;
  /**
   * The data type held in this RemixerItem.
   */
  private DataType dataType;

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
   * @param initialValue The initial value for this Variable.
   * @param context the object which created this variable, should be an activity.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param layoutId A layout to inflate when displaying this Variable in the UI.
   * @param dataType The data type this variable contains.
   */
  protected Variable(
      String title,
      String key,
      T initialValue,
      Object context,
      Callback<T> callback,
      int layoutId,
      DataType dataType) {
    this.title = title;
    this.key = key;
    this.context = new WeakReference<>(context);
    this.layoutId = layoutId;
    this.dataType = dataType;
    this.selectedValue = initialValue;
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

  public DataType getDataType() {
    return dataType;
  }

  public String getTitle() {
    return title;
  }

  public String getKey() {
    return key;
  }

  /**
   * Returns the layout id to inflate when displaying this variable.
   */
  public int getLayoutId() {
    return layoutId;
  }

  /**
   * Returns the context.
   */
  Object getContext() {
    return context.get();
  }

  /**
   * Set the current remixer instance. This allows the variable to notify other variables with the
   * same key.
   */
  public void setRemixer(Remixer remixer) {
    this.remixer = remixer;
  }

  /**
   * The currently selected value.
   */
  private T selectedValue;

  public T getSelectedValue() {
    return selectedValue;
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
   * Gets the serializable constraints string for this variable.
   */
  public String getSerializableConstraints() {
    return StoredVariable.VARIABLE_CONSTRAINT;
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
   * <p>On the other hand: key, dataType, and context are mandatory. If they're missing, an
   * {@link IllegalArgumentException} will be thrown.
   */
  public static class Builder<T> extends BaseVariableBuilder<Variable<T>, T> {

    @Override
    public Variable<T> build() {
      checkBaseFields();
      Variable<T> variable =
          new Variable<T>(title, key, initialValue, context, callback, layoutId, dataType);
      variable.init();
      return variable;
    }

    public Builder() {}
  }
}
