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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.ui.R;

/**
 * Displays an {@link Variable &lt;Boolean&gt;} as a switch.
 */
@Keep
public class BooleanVariableWidget extends LinearLayout
    implements RemixerWidget<Variable<Boolean>> {

  private TextView nameText;
  private Switch variableSwitch;

  public BooleanVariableWidget(Context context) {
    super(context);
  }

  public BooleanVariableWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BooleanVariableWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    variableSwitch = (Switch) findViewById(R.id.variableSwitch);
    nameText = (TextView) findViewById(R.id.variableName);
    nameText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        variableSwitch.toggle();
      }
    });
  }

  @Override
  public void bindVariable(@NonNull final Variable<Boolean> variable) {
    nameText.setText(variable.getTitle());
    variableSwitch.setChecked(variable.getSelectedValue());
    variableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        variable.setValue(b);
      }
    });
  }
}
