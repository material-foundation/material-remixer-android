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
package com.google.android.apps.remixer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.annotation.BooleanRemixMethod;
import com.google.android.libraries.remixer.annotation.RangeRemixMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.annotation.RemixerInstance;
import com.google.android.libraries.remixer.annotation.StringListRemixMethod;
import com.google.android.libraries.remixer.annotation.StringRemixMethod;
import com.google.android.libraries.remixer.ui.view.RemixerActivity;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityAnnotated extends AppCompatActivity implements RemixerActivity {

  @BindView(R.id.boundedText) TextView boundedText;
  @BindView(R.id.freeformText) TextView freeformText;
  @BindView(R.id.button) Button remixerButton;
  private RemixerFragment remixerFragment;

  @RemixerInstance Remixer remixer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    RemixerBinder.bind(this);
    remixerButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        getRemixerFragment().show(getSupportFragmentManager(), "Remixer");
      }
    });
  }

  @RangeRemixMethod(minValue = 10, maxValue = 72)
  void setTextSize(Integer size) {
    boundedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
  }

  @StringListRemixMethod(
      possibleValues = {"Hello World", "Alohomora", "Foo", "Bar", "May the force be with you"})
  void setBoundedText(String text) {
    boundedText.setText(text);
  }

  @BooleanRemixMethod
  void setFreeformVisible(Boolean visibility) {
    freeformText.setVisibility(visibility ? View.VISIBLE : View.GONE);
  }

  @StringRemixMethod(defaultValue = "Change me!")
  void setFreeformText(String text) {
    freeformText.setText(text);
  }

  @Override
  public Remixer getRemixer() {
    return remixer;
  }

  @NonNull
  private RemixerFragment getRemixerFragment() {
    if (remixerFragment == null) {
      remixerFragment = RemixerFragment.newInstance();
    }
    return remixerFragment;
  }
}
