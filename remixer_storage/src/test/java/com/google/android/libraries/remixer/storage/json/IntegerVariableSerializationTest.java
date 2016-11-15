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
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
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
    manifest = "src/main/AndroidManifest.xml")
public class IntegerVariableSerializationTest {

  private static String KEY = "key";

  private Variable<Integer> integerVariable;
  private ItemListVariable<Integer> integerListVariable;
  private RangeVariable rangeVariable;
  private Gson gson = GsonProvider.getInstance();

  @Before
  public void setUp() {
    integerVariable  = new Variable.Builder<Integer>()
        .setDefaultValue(0)
        .setKey(KEY)
        .setContext(this)
        .setDataType(DataType.NUMBER)
        .build();
    integerListVariable = new ItemListVariable.Builder<Integer>()
        .setPossibleValues(new Integer[]{Color.BLACK, Color.BLUE})
        .setDefaultValue(Color.BLACK)
        .setContext(this)
        .setKey(KEY)
        .setDataType(DataType.NUMBER)
        .build();
    rangeVariable = new RangeVariable.Builder()
        .setMinValue(0)
        .setMaxValue(100)
        .setIncrement(10)
        .setDefaultValue(10)
        .setContext(this)
        .setKey(KEY)
        .setDataType(DataType.NUMBER)
        .build();
  }

  @Test
  public void integerListVariableConvertsToStorageTest() {
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(integerListVariable);
    Assert.assertEquals(SupportedDataType.NUMBER.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, integerListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void modifiedIntegerListVariableConvertsToStorageTest() {
    integerListVariable.setValue(Color.BLUE);
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(integerListVariable);
    Assert.assertEquals(SupportedDataType.NUMBER.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, integerListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void integerVariableConvertsToStorageTest() {
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(integerVariable);
    Assert.assertEquals(SupportedDataType.NUMBER.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsVariable(result, integerVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void modifiedIntegerVariableConvertsToStorageTest() {
    integerListVariable.setValue(Color.BLUE);
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(integerVariable);
    Assert.assertEquals(SupportedDataType.NUMBER.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsVariable(result, integerVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void rangeVariableConvertsToStorageTest() {
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(rangeVariable);
    Assert.assertEquals(SupportedDataType.NUMBER.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsRangeVariable(result, rangeVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void modifiedRangeVariableConvertsToStorageTest() {
    rangeVariable.setValue(20);
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(rangeVariable);
    Assert.assertEquals(SupportedDataType.NUMBER.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsRangeVariable(result, rangeVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }
}
