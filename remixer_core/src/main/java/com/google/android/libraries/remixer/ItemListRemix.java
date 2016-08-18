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

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
   * @param title Displayable name for this Remix
   * @param key The key used to store this Remix
   * @param defaultValue The default value to use if none has been set.
   * @param values list of valid values.
   * @param callback Callback to run once the value is set. Can be null.
   * @param controlViewResourceId a layout id that renders this control on screen. Its root element
   * must implement {@code com.google.android.libraries.remixer.view.RemixView<ItemListRemixView<?>>}
   * @throws IllegalArgumentException if a list of valid values has been passed and the defaultValue
   * is not in it.
   */
  public ItemListRemix(
      String title,
      String key,
      T defaultValue,
      @NonNull List<T> values,
      @Nullable RemixCallback<String> callback,
      @LayoutRes int controlViewResourceId) {
    super(title, key, defaultValue, callback, controlViewResourceId);
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
}
