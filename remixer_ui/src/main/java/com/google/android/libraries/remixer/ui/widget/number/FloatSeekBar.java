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

package com.google.android.libraries.remixer.ui.widget.number;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * A seekbar that has float values and the possibility of a non-zero minimum value.
 *
 * This is a little hacky but the Android standard SeekBar is based on a ProgressBar, so its values
 * are all zero-based integers. We need a more versatile solution that supports float. To do this we
 * build on top of the existing seek bar, allowing us to set a minimum, maximum, and step sizes.
 */
public class FloatSeekBar extends SeekBar {

  private float min = 0f;
  private float max = 1f;
  private float stepSize = 0.01f;

  public FloatSeekBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setBoundaries(min, max, stepSize);
  }

  public FloatSeekBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    setBoundaries(min, max, stepSize);
  }

  public FloatSeekBar(Context context) {
    super(context);
    setBoundaries(min, max, stepSize);
  }

  /**
   * Set the float boundaries to {@code min} and {@code max} and uses the {@code stepSize} to
   * calculate the max progress for the underlying {@link SeekBar}.
   */
  public void setBoundaries(float min, float max, float stepSize) {
    this.min = min;
    this.max = max;
    this.stepSize = stepSize;
    int numberOfSteps = (int) ((max - min) / stepSize);
    setMax(numberOfSteps);
  }

  public void setValue(float value) {
    setProgress((int) ((value - min) / stepSize));
  }

  public float getValue() {
    return getProgress() * stepSize + min;
  }
}
