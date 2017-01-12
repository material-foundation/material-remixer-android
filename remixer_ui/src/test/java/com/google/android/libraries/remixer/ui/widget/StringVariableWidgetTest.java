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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.libraries.remixer.Callback;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.ui.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer.ui")
public class StringVariableWidgetTest {
  private static final String TITLE = "Color";
  private static final String KEY = "color";
  private static final String DEFAULT_VALUE = "red";

  @Mock
  Callback<String> mockCallback;

  private Variable<String> variable;
  private StringVariableWidget view;
  private TextView name;
  private EditText text;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    variable = new StringVariableBuilder()
        .setInitialValue(DEFAULT_VALUE)
        .setTitle(TITLE)
        .setKey(KEY)
        .setContext(this)
        .setCallback(mockCallback)
        .build();
    view = (StringVariableWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.string_variable_widget, null);
    view.bindVariable(variable);
    text = (EditText) view.findViewById(R.id.stringVariableText);
    name = (TextView) view.findViewById(R.id.variableName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(DEFAULT_VALUE, text.getText().toString());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(variable);
    text.setText("green");
    // After changing the text, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(variable);
  }
}
