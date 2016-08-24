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
   * {@code callback}. This uses the default layout to display this remix on-screen.
   *
   * @param title The name of this remix to be displayed in the UI.
   * @param key The key to store in SharedPreferences.
   * @param defaultValue The default value in case there is none in SharedPreferences.
   * @param minValue The minimum value for this remix.
   * @param maxValue The maximum value for this remix.
   * @param callback A callback to run when successfully initialized and when the value changes. Can
   *     be null.
   * @throws IllegalArgumentException {@code minValue > maxValue} or {@code defaultValue <
   *     minValue || defaultValue > maxValue }, meaning the defaultValue is out of range.
   */
  public RangeRemix(
      String title,
      String key,
      int defaultValue,
      int minValue,
      int maxValue,
      RemixCallback<Integer> callback) {
    this(title, key, defaultValue, minValue, maxValue, callback, 0);
  }

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
   * @param controlViewResourceId A layout id that renders this control on screen.
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
      int controlViewResourceId) {
    super(title, key, defaultValue, callback, controlViewResourceId);
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
}
