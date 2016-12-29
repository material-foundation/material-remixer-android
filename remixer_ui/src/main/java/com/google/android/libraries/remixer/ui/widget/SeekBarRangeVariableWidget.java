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

package com.google.android.libraries.remixer.ui.widget;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.ui.R;
import com.google.android.libraries.remixer.ui.widget.number.FloatSeekBar;

/**
 * Displays a {@link RangeVariable} as a SeekBar.
 */
@Keep
public class SeekBarRangeVariableWidget
    extends RelativeLayout implements RemixerWidget<RangeVariable> {

  private FloatSeekBar seekBar;
  private TextView nameText;
  private TextView currentValueText;
  private RangeVariable variable;

  public SeekBarRangeVariableWidget(Context context) {
    super(context);
  }

  public SeekBarRangeVariableWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SeekBarRangeVariableWidget(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    seekBar = (FloatSeekBar) findViewById(R.id.variableSeekBar);
    nameText = (TextView) findViewById(R.id.variableName);
    currentValueText = (TextView) findViewById(R.id.rangeVariableCurrentValue);
  }

  @Override
  public void bindVariable(@NonNull final RangeVariable variable) {
    this.variable = variable;
    nameText.setText(variable.getTitle());
    seekBar.setBoundaries(variable.getMinValue(), variable.getMaxValue(), variable.getIncrement());
    seekBar.setValue(variable.getSelectedValue());
    updateCurrentValue(seekBar.getValue());
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateCurrentValue(SeekBarRangeVariableWidget.this.seekBar.getValue());
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
  }

  private void updateCurrentValue(float value) {
    currentValueText.setText(String.valueOf(value));
    if (variable.getSelectedValue() != value) {
      variable.setValue(value);
    }
  }
}
