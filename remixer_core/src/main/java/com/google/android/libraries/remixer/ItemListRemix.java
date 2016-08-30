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

import java.util.Arrays;
import java.util.List;

/**
 * A Remix that lets you choose from a list of pre-set values.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class ItemListRemix<T> extends Remix<T> {

  private final List<T> valueList;

  /**
   * Creates a new ItemListRemix, checks its default value and runs the callback if the value is
   * valid.
   *
   * @param title Displayable name for this Remix.
   * @param key The key used to store this Remix.
   * @param defaultValue The default value to use if none has been set.
   * @param values List of valid values.
   * @param callback Callback to run once the value is set. Can be null.
   * @param layoutId A layout id that renders this control on screen.
   * @throws IllegalArgumentException A list of valid values has been passed and the defaultValue
   *     is not in it.
   */
  public ItemListRemix(
      String title,
      String key,
      T defaultValue,
      List<T> values,
      RemixCallback<T> callback,
      int layoutId) {
    super(title, key, defaultValue, callback, layoutId);
    this.valueList = values;
    checkValue(defaultValue);
    runCallback();
  }

  @Override
  protected void checkValue(T value) {
    if (!valueList.contains(value)) {
      throw new IllegalArgumentException(
          String.format("%s is not a valid value for Remix %s", value, getKey()));
    }
  }

  public List<T> getValueList() {
    return valueList;
  }

  /**
   * Convenience builder for ItemListRemix.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the default value is not set, the first value of the list will be used as the default
   * value.
   * <li>If the layout id is not set, the default layout will be used.
   * <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key and possibleValues are mandatory. If either is missing, an
   * {@link IllegalArgumentException} will be thrown.
   */
  public static class Builder<T> {

    private String key;
    private String title;
    private T defaultValue;
    private List<T> possibleValues;
    private RemixCallback<T> callback;
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

    public Builder<T> setPossibleValues(List<T> possibleValues) {
      this.possibleValues = possibleValues;
      return this;
    }

    public Builder<T> setPossibleValues(T... possibleValues) {
      this.possibleValues = Arrays.asList(possibleValues);
      return this;
    }

    public Builder<T> setCallback(RemixCallback<T> callback) {
      this.callback = callback;
      return this;
    }

    public Builder<T> setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new ItemListRemix created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key or possibleValues are missing or if the configuration
     *     is invalid for ItemListRemix.
     */
    public ItemListRemix<T> build() {
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for ItemListRemix");
      }
      if (possibleValues == null || possibleValues.isEmpty()) {
        throw new IllegalArgumentException(
            "possibleValues cannot be unset or empty for ItemListRemix");
      }
      if (defaultValue == null) {
        defaultValue = possibleValues.get(0);
      }
      if (title == null) {
        title = key;
      }
      return new ItemListRemix<T>(title, key, defaultValue, possibleValues, callback, layoutId);
    }
  }
}
