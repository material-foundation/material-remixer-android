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

package com.google.android.libraries.remixer.serialization;

import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.InitializationHelper;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.Variable;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SerializableRemixerContentsSerializationTest {

  private static final int BLACK = 0x00000000;
  private static final int BLUE = 0xFF0000FF;

  private Variable<Boolean> booleanVariable;
  private ItemListVariable<Integer> colorListVariable;
  private Variable<Float> floatVariable;
  private ItemListVariable<Float> floatListVariable;
  private RangeVariable rangeVariable;
  private ItemListVariable<String> stringListVariable;
  private Variable<String> stringVariable;
  private SerializableRemixerContents serializableRemixerContents;
  private Gson gson = GsonProvider.getInstance();

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init();
  }

  @Before
  public void setUp() {
    serializableRemixerContents = new SerializableRemixerContents();
    booleanVariable = new BooleanVariableBuilder()
        .setContext(this)
        .setKey("boolean")
        .build();
    serializableRemixerContents.addItem(booleanVariable);
    colorListVariable = new ItemListVariable.Builder<Integer>()
        .setLimitedToValues(new Integer[]{BLACK, BLUE})
        .setInitialValue(BLACK)
        .setContext(this)
        .setKey("colorList")
        .setDataType(DataType.COLOR)
        .build();
    serializableRemixerContents.addItem(colorListVariable);
    floatVariable  = new Variable.Builder<Float>()
        .setInitialValue(0f)
        .setKey("float")
        .setContext(this)
        .setDataType(DataType.NUMBER)
        .build();
    serializableRemixerContents.addItem(floatVariable);
    floatListVariable = new ItemListVariable.Builder<Float>()
        .setLimitedToValues(new Float[]{12f, 24f})
        .setInitialValue(12f)
        .setContext(this)
        .setKey("floatList")
        .setDataType(DataType.NUMBER)
        .build();
    serializableRemixerContents.addItem(floatListVariable);
    rangeVariable = new RangeVariable.Builder()
        .setMinValue(0f)
        .setIncrement(10f)
        .setMaxValue(100f)
        .setInitialValue(10f)
        .setContext(this)
        .setKey("range")
        .build();
    serializableRemixerContents.addItem(rangeVariable);
    stringVariable = new StringVariableBuilder()
        .setContext(this)
        .setKey("string")
        .build();
    serializableRemixerContents.addItem(stringVariable);
    stringListVariable = new ItemListVariable.Builder<String>()
        .setLimitedToValues(new String[]{"a", "b", "c"})
        .setInitialValue("a")
        .setContext(this)
        .setKey("stringList")
        .setDataType(DataType.STRING)
        .build();
    serializableRemixerContents.addItem(stringListVariable);
  }

  @Test
  public void remixerContentsSerializesAndDeserializesTest() {
    Assert.assertEquals(
        serializableRemixerContents,
        gson.fromJson(gson.toJsonTree(serializableRemixerContents),
            SerializableRemixerContents.class));
  }

  @Test
  public void modifiedRemixerContentsSerializesAndDeserializesTest() {
    booleanVariable.setValue(true);
    serializableRemixerContents.setValue(booleanVariable);
    Assert.assertEquals(
        booleanVariable.getSelectedValue(),
        serializableRemixerContents.getItem(booleanVariable.getKey()).selectedValue);
    stringVariable.setValue("SOMENEWVALUE");
    serializableRemixerContents.setValue(stringVariable);
    Assert.assertEquals(
        stringVariable.getSelectedValue(),
        serializableRemixerContents.getItem(stringVariable.getKey()).selectedValue);
    Assert.assertEquals(
        serializableRemixerContents,
        gson.fromJson(gson.toJsonTree(serializableRemixerContents),
            SerializableRemixerContents.class));
  }
}
