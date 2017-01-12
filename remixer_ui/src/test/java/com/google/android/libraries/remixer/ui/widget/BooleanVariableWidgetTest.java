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
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.Callback;
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
public class BooleanVariableWidgetTest {
  private static final String TITLE = "Some boolean";
  private static final String KEY = "theboolean";
  private static final boolean DEFAULT_VALUE = false;

  @Mock
  Callback<Boolean> mockCallback;

  private Variable<Boolean> variable;
  private BooleanVariableWidget view;
  private TextView name;
  private Switch variableSwitch;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    variable = new BooleanVariableBuilder()
        .setInitialValue(DEFAULT_VALUE)
        .setTitle(TITLE)
        .setKey(KEY)
        .setContext(this)
        .setCallback(mockCallback)
        .build();
    view = (BooleanVariableWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.boolean_variable_widget, null);
    view.bindVariable(variable);
    variableSwitch = (Switch) view.findViewById(R.id.variableSwitch);
    name = (TextView) view.findViewById(R.id.variableName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(DEFAULT_VALUE, variableSwitch.isChecked());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(variable);
    variableSwitch.toggle();
    // After changing the text, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(variable);
  }

  @Test
  public void callbackIsCalledOnNameClick() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(variable);
    name.performClick();
    // After changing the text, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(variable);
  }
}
