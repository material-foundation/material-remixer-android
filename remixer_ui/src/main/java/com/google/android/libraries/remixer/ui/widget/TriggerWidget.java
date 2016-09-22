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
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.ui.R;

/**
 * Displays a {@link Trigger} as a button.
 */
@Keep
public class TriggerWidget extends RelativeLayout implements RemixerItemWidget<Trigger> {

  private Button button;

  public TriggerWidget(Context context) {
    super(context);
  }

  public TriggerWidget(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TriggerWidget(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    button = (Button) findViewById(R.id.triggerButton);
  }

  @Override
  public void bindRemixerItem(@NonNull final Trigger trigger) {
    button.setText(trigger.getTitle());
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        trigger.trigger();
      }
    });
  }
}
