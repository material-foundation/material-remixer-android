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
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.libraries.remixer.Callback;
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
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
public class ItemListVariableWidgetTest {
  private static final String TITLE = "Color";
  private static final String KEY = "color";
  private static final String[] ITEM_LIST = new String[]{
      "red",
      "blue",
      "yellow",
      "green"
  };
  private static final int DEFAULT_VALUE_INDEX = 2;

  @Mock
  Callback<String> mockCallback;

  private ItemListVariable<String> variable;
  private ItemListVariableWidget view;
  private TextView name;
  private Spinner spinner;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    variable = new ItemListVariable.Builder<String>()
        .setLimitedToValues(ITEM_LIST)
        .setInitialValue(ITEM_LIST[DEFAULT_VALUE_INDEX])
        .setTitle(TITLE)
        .setKey(KEY)
        .setContext(this)
        .setCallback(mockCallback)
        .setDataType(DataType.STRING)
        .build();
    view = (ItemListVariableWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.item_list_variable_widget, null);
    view.bindVariable(variable);
    spinner = (Spinner) view.findViewById(R.id.variableSpinner);
    name = (TextView) view.findViewById(R.id.variableName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(ITEM_LIST[DEFAULT_VALUE_INDEX], spinner.getSelectedItem());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(variable);
    spinner.setSelection(0);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[0], spinner.getSelectedItem());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(variable);
    spinner.setSelection(1);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[1], spinner.getSelectedItem());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(3)).onValueSet(variable);
  }
}
