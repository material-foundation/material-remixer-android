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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.ui.R;

/**
 * Displays as {@link Variable &lt;String&gt;} in an EditText and lets you edit its value.
 */
@Keep
public class StringVariableWidget extends RelativeLayout
    implements RemixerWidget<Variable<String>> {

  private TextView nameText;
  private EditText text;

  public StringVariableWidget(Context context) {
    super(context);
  }

  public StringVariableWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StringVariableWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    text = (EditText) findViewById(R.id.stringVariableText);
    nameText = (TextView) findViewById(R.id.variableName);
  }

  @Override
  public void bindVariable(@NonNull final Variable<String> variable) {
    nameText.setText(variable.getTitle());
    text.setText(variable.getSelectedValue());
    text.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        variable.setValue(s.toString());
      }
    });
  }
}
