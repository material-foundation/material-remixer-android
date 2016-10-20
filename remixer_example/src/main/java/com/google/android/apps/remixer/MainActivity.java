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
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.annotation.StringListVariableMethod;
import com.google.android.libraries.remixer.annotation.StringVariableMethod;
import com.google.android.libraries.remixer.annotation.TriggerMethod;
import com.google.android.libraries.remixer.ui.gesture.Direction;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * Annotated version of the BoxActivity.
 */
public class MainActivity extends AppCompatActivity {

  TextView titleText;
  TextView freeformText;
  Button remixerButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    titleText = (TextView) findViewById(R.id.titleText);
    freeformText = (TextView) findViewById(R.id.freeformText);
    remixerButton = (Button) findViewById(R.id.button);
    RemixerBinder.bind(this);

    RemixerFragment remixerFragment = RemixerFragment.newInstance();
    remixerFragment.attachToGesture(this, Direction.UP, 3);
    remixerFragment.attachToButton(this, remixerButton);
  }

  @RangeVariableMethod(
      minValue = 16, maxValue = 72, increment = 4, title = "(Shared) Title font size")
  void setTitleTextSize(Integer size) {
    titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
  }

  @StringListVariableMethod(
      title = "Title text",
      possibleValues = {"Hello World", "Alohomora", "Foo", "Bar", "May the force be with you"})
  void setTitleText(String text) {
    titleText.setText(text);
  }

  @StringVariableMethod(defaultValue = "Change me!")
  void setFreeformText(String text) {
    freeformText.setText(text);
  }

  @TriggerMethod
  void toast() {
    Toast.makeText(this, freeformText.getText(), Toast.LENGTH_SHORT).show();
  }
}
