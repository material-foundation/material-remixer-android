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
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.ui.R;

/**
 * Displays as {@link Remix&lt;String&gt;} in an EditText and lets you edit its value.
 */
@Keep
public class StringRemixWidget extends RelativeLayout
    implements RemixerItemWidget<Remix<String>> {

  private TextView nameText;
  private EditText text;

  public StringRemixWidget(Context context) {
    super(context);
  }

  public StringRemixWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StringRemixWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    text = (EditText) findViewById(R.id.stringRemixText);
    nameText = (TextView) findViewById(R.id.stringRemixName);
  }

  @Override
  public void bindRemixerItem(@NonNull final Remix<String> remix) {
    nameText.setText(remix.getTitle());
    text.setText(remix.getSelectedValue());
    text.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        remix.setValue(s.toString());
      }
    });
  }
}
