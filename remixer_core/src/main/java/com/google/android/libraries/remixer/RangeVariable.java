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
public class RangeVariable extends Variable<Integer> {

  protected static final String INVALID_RANGE_ERROR_FORMAT =
      "Invalid range for Variable %s min: %d, max: %d";
  protected static final String NEGATIVE_STEPPING_ERROR_FORMAT =
      "Stepping must be >= 1, Variable %s has increment %d";
  protected static final String STEP_INCREMENT_INVALID_FOR_RANGE_ERROR_FORMAT =
      "Variable %s: incorrect increment, can't get to %s %d from minValue %d using"
          + " increment %d";
  protected static final String NEW_VALUE_OUT_OF_BOUNDS_ERROR_FORMAT =
      "%d is out of bounds for Variable %s: min: %d, max: %d";
  private final int minValue;
  private final int maxValue;
  private final int increment;

  /**
   * Constructor that checks correctness of the range, validates {@code defaultValue} and runs
   * {@code callback}.
   *
   * @param title The name of this remix to be displayed in the UI.
   * @param key The key to store in SharedPreferences.
   * @param defaultValue The default value in case there is none in SharedPreferences.
   * @param minValue The minimum value for this remix.
   * @param maxValue The maximum value for this remix.
   * @param increment A value that defines each step. Must be a positive integer. So if you have
   *     {@code minValue = 0 && maxValue = 12 && increment = 4}, only 0, 4, 8, 12 are possible
   *     values.
   * @param parentObject the object which created this variable, should be an activity.
   * @param callback A callback to run when successfully initialized and when the value changes. Can
   *     be null.
   * @param layoutId A layout id that renders this control on screen.
   * @throws IllegalArgumentException {@code minValue > maxValue} or {@code increment < 1} or {@code
   *     (maxValue - minValue) % increment != 0} which means the current increment setting can't
   *     possibly get from minValue to maxValue.
   */
  public RangeVariable(
      String title,
      String key,
      int defaultValue,
      int minValue,
      int maxValue,
      int increment,
      Object parentObject,
      Callback<Integer> callback,
      int layoutId) {
    super(title, key, defaultValue, parentObject, callback, layoutId);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.increment = increment;
    checkRange();
    checkStepIncrement();
  }

  private void checkRange() {
    if (minValue > maxValue) {
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              INVALID_RANGE_ERROR_FORMAT,
              getTitle(),
              minValue,
              maxValue));
    }
  }

  private void checkStepIncrement() {
    if (increment < 1) {
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              NEGATIVE_STEPPING_ERROR_FORMAT,
              getTitle(),
              increment));
    }
    checkValueAndStep(maxValue, "maxValue");
  }

  private void checkValueAndStep(int value, String valueName) {
    if ((value - minValue) % increment != 0) {
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              STEP_INCREMENT_INVALID_FOR_RANGE_ERROR_FORMAT,
              getTitle(),
              valueName,
              value,
              minValue,
              increment));
    }
  }

  @Override
  protected void checkValue(Integer newValue) {
    if (newValue < minValue || newValue > maxValue) {
      throw new IllegalArgumentException(
          String.format(
              Locale.getDefault(),
              NEW_VALUE_OUT_OF_BOUNDS_ERROR_FORMAT,
              newValue,
              getTitle(),
              minValue,
              maxValue));
    }
    checkValueAndStep(newValue, "newValue");
  }

  public int getMinValue() {
    return minValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public int getIncrement() {
    return increment;
  }

  /**
   * Convenience builder for RangeVariable, the number of arguments for the constructor is too
   * large.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the default value is not set, minValue will be used as the default value.
   * <li>If the increment is not set, 1 will be used.
   * <li>If the layout id is not set, the default layout will be used.
   * <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key, minValue and maxValue are mandatory. If any of these are missing or
   * the settings are incorrect according to the logic of {@link RangeVariable} an
   * {@link IllegalArgumentException} will be thrown.
   */
  public static class Builder {

    private String key;
    private String title;
    private Integer defaultValue;
    private Integer minValue;
    private Integer maxValue;
    private int increment = 1;
    private Object parentObject;
    private Callback<Integer> callback;
    private int layoutId = 0;

    public Builder() {}

    public Builder setKey(String key) {
      this.key = key;
      return this;
    }

    public Builder setParentObject(Object parentObject) {
      this.parentObject = parentObject;
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

    public Builder setIncrement(int increment) {
      this.increment = increment;
      return this;
    }

    public Builder setDefaultValue(int defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder setCallback(Callback<Integer> callback) {
      this.callback = callback;
      return this;
    }

    public Builder setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new RangeVariable created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key, minValue or maxValue are missing, or if these
     *     settings are incorrect for {@link RangeVariable}
     */
    public RangeVariable buildAndInit() {
      if (minValue == null || maxValue == null) {
        throw new IllegalArgumentException("minValue and maxValue must not be null");
      }
      if (defaultValue == null) {
        defaultValue = minValue;
      }
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for RangeVariable");
      }
      if (parentObject == null) {
        throw new IllegalArgumentException("parentObject cannot be unset for RangeVariable");
      }
      if (title == null) {
        title = key;
      }
      RangeVariable remix = new RangeVariable(
          title, key, defaultValue, minValue, maxValue, increment, parentObject, callback,
          layoutId);
      remix.init();
      return remix;
    }
  }
}
