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
import java.util.Arrays;
import java.util.List;

/**
 * A Variable that lets you choose from a list of pre-set values.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class ItemListVariable<T> extends Variable<T> {

  private final List<T> limitedToValues;

  /**
   * Creates a new ItemListVariable, checks its initial value and runs the callback if the value is
   * valid.
   *
   * @param title Displayable name for this Variable.
   * @param key The key used to store this Variable.
   * @param initialValue The initial value to use if none has been set.
   * @param limitedToValues List of valid values that this variable is limited to take.
   * @param context the object which created this variable, should be an activity.
   * @param callback Callback to run once the value is set. Can be null.
   * @param layoutId A layout id that renders this control on screen.
   * @param dataType The data type this variable contains.
   */
  private ItemListVariable(
      String title,
      String key,
      T initialValue,
      List<T> limitedToValues,
      Object context,
      Callback<T> callback,
      int layoutId,
      DataType dataType) {
    super(title, key, initialValue, context, callback, layoutId, dataType);
    this.limitedToValues = limitedToValues;
  }

  @Override
  protected void checkValue(T value) {
    if (!limitedToValues.contains(value)) {
      throw new IllegalArgumentException(
          String.format("%s is not a valid value for Variable %s", value, getKey()));
    }
  }

  public List<T> getLimitedToValues() {
    return limitedToValues;
  }

  /**
   * Gets the serializable constraints string for this variable.
   */
  public String getSerializableConstraints() {
    return StoredVariable.ITEM_LIST_VARIABLE_CONSTRAINT;
  }

  /**
   * Convenience builder for ItemListVariable.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   *   <li>If the initial value is not set, the first value of the list will be used as the default
   *   value.
   *   <li>If the layout id is not set, the default layout will be used.
   *   <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key, dataType, context, and limitedToValues are mandatory. If either is
   * missing, an {@link IllegalArgumentException} will be thrown.
   */
  public static class Builder<T> extends BaseVariableBuilder<ItemListVariable<T>, T> {

    private List<T> limitedToValues;

    public Builder<T> setLimitedToValues(List<T> limitedToValues) {
      this.limitedToValues = limitedToValues;
      return this;
    }

    public Builder<T> setLimitedToValues(T[] limitedToValues) {
      this.limitedToValues = Arrays.asList(limitedToValues);
      return this;
    }

    /**
     * Returns a new ItemListVariable created with the configuration stored in this builder
     * instance.
     *
     * @throws IllegalArgumentException If key or limitedToValues are missing or if the
     *     configuration is invalid for ItemListVariable.
     */
    @Override
    public ItemListVariable<T> build() {
      checkBaseFields();
      if (limitedToValues == null || limitedToValues.isEmpty()) {
        throw new IllegalArgumentException(
            "limitedToValues cannot be unset or empty for ItemListVariable");
      }
      if (initialValue == null) {
        initialValue = limitedToValues.get(0);
      }
      ItemListVariable<T> variable = new ItemListVariable<T>(
          title, key, initialValue, limitedToValues, context, callback, layoutId, dataType);
      variable.init();
      return variable;
    }
  }
}
