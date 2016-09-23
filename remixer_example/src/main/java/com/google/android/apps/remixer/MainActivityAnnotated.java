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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.annotation.BooleanRemixMethod;
import com.google.android.libraries.remixer.annotation.IntegerListRemixMethod;
import com.google.android.libraries.remixer.annotation.RangeRemixMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.annotation.RemixerInstance;
import com.google.android.libraries.remixer.annotation.StringListRemixMethod;
import com.google.android.libraries.remixer.annotation.StringRemixMethod;
import com.google.android.libraries.remixer.annotation.TriggerMethod;
import com.google.android.libraries.remixer.ui.gesture.Direction;
import com.google.android.libraries.remixer.ui.view.RemixerActivity;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * Annotated version of the MainActivity.
 *
 * <p>Notice implementing RemixerActivity is necessary to use RemixerFragment.
 */
public class MainActivityAnnotated extends AppCompatActivity implements RemixerActivity {

  TextView boundedText;
  TextView freeformText;
  Button remixerButton;

  @RemixerInstance Remixer remixer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    boundedText = (TextView) findViewById(R.id.boundedText);
    freeformText = (TextView) findViewById(R.id.freeformText);
    remixerButton = (Button) findViewById(R.id.button);
    RemixerBinder.bind(this);

    RemixerFragment remixerFragment = RemixerFragment.newInstance();
    remixerFragment.attachToGesture(this, Direction.UP, 3);
    remixerFragment.attachToButton(this, remixerButton);
  }

  @IntegerListRemixMethod(
      possibleValues = {Color.DKGRAY, Color.LTGRAY, Color.MAGENTA, Color.CYAN},
      layoutId = R.layout.color_list_remix_widget
  )
  void setTextColor(@ColorInt Integer color) {
    boundedText.setTextColor(color);
    freeformText.setTextColor(color);
  }

  @RangeRemixMethod(minValue = 16, maxValue = 72, increment = 4)
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

  @TriggerMethod
  void toast() {
    Toast.makeText(this, freeformText.getText(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public Remixer getRemixer() {
    return remixer;
  }
}
