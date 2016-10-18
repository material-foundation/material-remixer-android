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

package com.google.android.libraries.remixer.storage.json;

import android.graphics.Color;
import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.Variable;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer.ui")
public class RemixerStatusSerializationTest {

  private Variable<Boolean> booleanVariable;
  private ItemListVariable<Integer> colorListVariable;
  private Variable<Integer> integerVariable;
  private ItemListVariable<Integer> integerListVariable;
  private RangeVariable rangeVariable;
  private ItemListVariable<String> stringListVariable;
  private Variable<String> stringVariable;
  private RemixerStatus status;
  private Gson gson = GsonProvider.getInstance();

  @Before
  public void setUp() {
    status = new RemixerStatus();
    booleanVariable = new BooleanVariableBuilder()
        .setParentObject(this)
        .setKey("boolean")
        .buildAndInit();
    status.addItem(booleanVariable);
    colorListVariable = new ItemListVariable.Builder<Integer>()
        .setParentObject(this)
        .setKey("colorList")
        .setDefaultValue(Color.BLACK)
        .setPossibleValues(new Integer[]{Color.BLACK, Color.BLUE})
        .setLayoutId(com.google.android.libraries.remixer.ui.R.layout.color_list_variable_widget)
        .buildAndInit();
    status.addItem(colorListVariable);
    integerVariable  = new Variable.Builder<Integer>()
        .setDefaultValue(0)
        .setKey("integer")
        .setParentObject(this)
        .buildAndInit();
    status.addItem(integerVariable);
    integerListVariable = new ItemListVariable.Builder<Integer>()
        .setParentObject(this)
        .setKey("integerList")
        .setDefaultValue(Color.BLACK)
        .setPossibleValues(new Integer[]{Color.BLACK, Color.BLUE})
        .buildAndInit();
    status.addItem(integerListVariable);
    rangeVariable = new RangeVariable.Builder()
        .setParentObject(this)
        .setKey("range")
        .setDefaultValue(10)
        .setMinValue(0)
        .setIncrement(10)
        .setMaxValue(100)
        .buildAndInit();
    status.addItem(rangeVariable);
    stringVariable = new StringVariableBuilder()
        .setParentObject(this)
        .setKey("string")
        .buildAndInit();
    status.addItem(stringVariable);
    stringListVariable = new ItemListVariable.Builder<String>()
        .setParentObject(this)
        .setKey("stringList")
        .setDefaultValue("a")
        .setPossibleValues(new String[]{"a", "b", "c"})
        .buildAndInit();
    status.addItem(stringListVariable);
  }

  @Test
  public void remixerStatusSerializesAndDeserializesTest() {
    Assert.assertEquals(status, gson.fromJson(gson.toJsonTree(status), RemixerStatus.class));
  }

  @Test
  public void modifiedRemixerStatusSerializesAndDeserializesTest() {
    booleanVariable.setValue(true);
    status.addItem(booleanVariable);
    Assert.assertEquals(
        booleanVariable.getSelectedValue(), status.getItem(booleanVariable.getKey()).selectedValue);
    stringVariable.setValue("SOMENEWVALUE");
    status.addItem(stringVariable);
    Assert.assertEquals(
        stringVariable.getSelectedValue(), status.getItem(stringVariable.getKey()).selectedValue);
    Assert.assertEquals(status, gson.fromJson(gson.toJsonTree(status), RemixerStatus.class));
  }
}
