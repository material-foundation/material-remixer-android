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
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.libraries.remixer.BooleanRemix;
import com.google.android.libraries.remixer.ui.R;

/**
 * Displays a {@link BooleanRemix} as a switch.
 */
public class BooleanRemixWidget extends RelativeLayout implements RemixerItemWidget<BooleanRemix> {

  private TextView nameText;
  private Switch remixSwitch;

  public BooleanRemixWidget(Context context) {
    super(context);
  }

  public BooleanRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BooleanRemixWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    remixSwitch = (Switch) findViewById(R.id.booleanRemixSwitch);
    nameText = (TextView) findViewById(R.id.booleanRemixName);
  }

  @Override
  public void bindRemixerItem(@NonNull final BooleanRemix remix) {
    nameText.setText(remix.getTitle());
    remixSwitch.setChecked(remix.getSelectedValue());
    remixSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        remix.setValue(b);
      }
    });
  }
}
