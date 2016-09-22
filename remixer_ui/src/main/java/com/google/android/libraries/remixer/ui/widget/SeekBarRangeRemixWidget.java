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
import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.ui.R;

/**
 * Displays a {@link RangeRemix} as a SeekBar.
 */
@Keep
public class SeekBarRangeRemixWidget
    extends RelativeLayout implements RemixerItemWidget<RangeRemix> {

  private SeekBar seekBar;
  private TextView nameText;
  private TextView currentValueText;
  private RangeRemix remix;

  public SeekBarRangeRemixWidget(Context context) {
    super(context);
  }

  public SeekBarRangeRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SeekBarRangeRemixWidget(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    seekBar = (SeekBar) findViewById(R.id.rangeRemixSeekBar);
    nameText = (TextView) findViewById(R.id.rangeRemixName);
    currentValueText = (TextView) findViewById(R.id.rangeRemixCurrentValue);
  }

  @Override
  public void bindRemixerItem(@NonNull final RangeRemix remix) {
    this.remix = remix;
    nameText.setText(remix.getTitle());
    int maxProgress = remix.getMaxValue() - remix.getMinValue();
    maxProgress /= remix.getIncrement();
    seekBar.setMax(maxProgress);
    int progress = remix.getSelectedValue() - remix.getMinValue();
    progress /= remix.getIncrement();
    seekBar.setProgress(progress);
    updateCurrentValue(progress);
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateCurrentValue(progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
  }

  private void updateCurrentValue(int progress) {
    int value = remix.getMinValue() + progress * remix.getIncrement();
    currentValueText.setText(String.valueOf(value));
    if (remix.getSelectedValue() != value) {
      remix.setValue(value);
    }
  }
}
