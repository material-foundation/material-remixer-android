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
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Remixer;
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
  private Variable<Integer> integerVariable;
  private ItemListVariable<Integer> integerListVariable;
  private RangeVariable rangeVariable;
  private ItemListVariable<String> stringListVariable;
  private Variable<String> stringVariable;
  private SerializableRemixerContents serializableRemixerContents;
  private Gson gson = GsonProvider.getInstance();

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init(Remixer.getInstance());
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
        .setPossibleValues(new Integer[]{BLACK, BLUE})
        .setDefaultValue(BLACK)
        .setContext(this)
        .setKey("colorList")
        .setDataType(DataType.COLOR)
        .build();
    serializableRemixerContents.addItem(colorListVariable);
    integerVariable  = new Variable.Builder<Integer>()
        .setDefaultValue(0)
        .setKey("integer")
        .setContext(this)
        .setDataType(DataType.NUMBER)
        .build();
    serializableRemixerContents.addItem(integerVariable);
    integerListVariable = new ItemListVariable.Builder<Integer>()
        .setPossibleValues(new Integer[]{12, 24})
        .setDefaultValue(12)
        .setContext(this)
        .setKey("integerList")
        .setDataType(DataType.NUMBER)
        .build();
    serializableRemixerContents.addItem(integerListVariable);
    rangeVariable = new RangeVariable.Builder()
        .setMinValue(0)
        .setIncrement(10)
        .setMaxValue(100)
        .setDefaultValue(10)
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
        .setPossibleValues(new String[]{"a", "b", "c"})
        .setDefaultValue("a")
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
    serializableRemixerContents.addItem(booleanVariable);
    Assert.assertEquals(
        booleanVariable.getSelectedValue(),
        serializableRemixerContents.getItem(booleanVariable.getKey()).selectedValue);
    stringVariable.setValue("SOMENEWVALUE");
    serializableRemixerContents.addItem(stringVariable);
    Assert.assertEquals(
        stringVariable.getSelectedValue(),
        serializableRemixerContents.getItem(stringVariable.getKey()).selectedValue);
    Assert.assertEquals(
        serializableRemixerContents,
        gson.fromJson(gson.toJsonTree(serializableRemixerContents),
            SerializableRemixerContents.class));
  }
}
