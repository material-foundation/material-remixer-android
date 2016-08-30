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

import java.util.Locale;

/**
 * An integer value that can be adjusted.
 *
 * <p>It also checks that values are always in the range specified by [minValue,maxValue].
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class RangeRemix extends Remix<Integer> {

  private final int minValue;
  private final int maxValue;

  /**
   * Constructor that checks correctness of the range, validates {@code defaultValue} and runs
   * {@code callback}.
   *
   * @param title The name of this remix to be displayed in the UI.
   * @param key The key to store in SharedPreferences.
   * @param defaultValue The default value in case there is none in SharedPreferences.
   * @param minValue The minimum value for this remix.
   * @param maxValue The maximum value for this remix.
   * @param callback A callback to run when successfully initialized and when the value changes. Can
   *     be null.
   * @param layoutId A layout id that renders this control on screen.
   * @throws IllegalArgumentException {@code minValue > maxValue} or {@code defaultValue <
   *     minValue || defaultValue > maxValue }, meaning the defaultValue is out of range.
   */
  public RangeRemix(
      String title,
      String key,
      int defaultValue,
      int minValue,
      int maxValue,
      RemixCallback<Integer> callback,
      int layoutId) {
    super(title, key, defaultValue, callback, layoutId);
    this.minValue = minValue;
    this.maxValue = maxValue;
    checkRange();
    checkValue(defaultValue);
    runCallback();
  }

  private void checkRange() {
    if (minValue > maxValue) {
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              "Invalid range for Remix %s min: %d, max: %d",
              getTitle(),
              minValue,
              maxValue));
    }
  }

  @Override
  protected void checkValue(Integer newValue) {
    // TODO(miguely): Check for correct stepping if specified.
    if (newValue < minValue || newValue > maxValue) {
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              "%d is out of bounds for Remix %s: min: %d, max: %d",
              newValue,
              getTitle(),
              minValue,
              maxValue));
    }
  }

  public int getMinValue() {
    return minValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  /**
   * Convenience builder for RangeRemix, the number of arguments for the constructor is too large.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the default value is not set, minValue will be used as the default value.
   * <li>If the layout id is not set, the default layout will be used.
   * <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key, minValue and maxValue are mandatory. If any of these are missing or
   * the settings are incorrect according to the logic of {@link RangeRemix} an
   * {@link IllegalArgumentException} will be thrown.
   */
  public static class Builder {

    private String key;
    private String title;
    private Integer defaultValue;
    private Integer minValue;
    private Integer maxValue;
    private RemixCallback<Integer> callback;
    private int layoutId = 0;

    public Builder() {}

    public Builder setKey(String key) {
      this.key = key;
      return this;
    }

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setMinValue(int minValue) {
      this.minValue = minValue;
      return this;
    }

    public Builder setMaxValue(int maxValue) {
      this.maxValue = maxValue;
      return this;
    }

    public Builder setDefaultValue(int defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder setCallback(RemixCallback<Integer> callback) {
      this.callback = callback;
      return this;
    }

    public Builder setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new RangeRemix created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key, minValue or maxValue are missing, or if these
     *     settings are incorrect for {@link RangeRemix}
     */
    public RangeRemix build() {
      if (minValue == null || maxValue == null) {
        throw new IllegalArgumentException("minValue and maxValue must not be null");
      }
      if (defaultValue == null) {
        defaultValue = minValue;
      }
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for RangeRemix");
      }
      if (title == null) {
        title = key;
      }
      return new RangeRemix(title, key, defaultValue, minValue, maxValue, callback, layoutId);
    }
  }
}
