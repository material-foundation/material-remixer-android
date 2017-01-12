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

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.InitializationHelper;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Variable;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NumberVariableSerializationTest {

  private static String KEY = "key";

  private Variable<Float> floatVariable;
  private ItemListVariable<Float> floatListVariable;
  private RangeVariable rangeVariable;
  private Gson gson = GsonProvider.getInstance();

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init();
  }

  @Before
  public void setUp() {
    floatVariable = new Variable.Builder<Float>()
        .setInitialValue(0f)
        .setKey(KEY)
        .setContext(this)
        .setDataType(DataType.NUMBER)
        .build();
    floatListVariable = new ItemListVariable.Builder<Float>()
        .setLimitedToValues(new Float[]{12f, 154f})
        .setInitialValue(12f)
        .setContext(this)
        .setKey(KEY)
        .setDataType(DataType.NUMBER)
        .build();
    rangeVariable = new RangeVariable.Builder()
        .setMinValue(0f)
        .setMaxValue(100f)
        .setIncrement(10f)
        .setInitialValue(10f)
        .setContext(this)
        .setKey(KEY)
        .setDataType(DataType.NUMBER)
        .build();
  }

  @Test
  public void numberListVariableConvertsToStorageTest() {
    StoredVariable<Float> result = StoredVariable.fromVariable(floatListVariable);
    Assert.assertEquals(DataType.NUMBER.getName(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, floatListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void modifiedFloatListVariableConvertsToStorageTest() {
    floatListVariable.setValue(154f);
    StoredVariable<Float> result = StoredVariable.fromVariable(floatListVariable);
    Assert.assertEquals(DataType.NUMBER.getName(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, floatListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void integerVariableConvertsToStorageTest() {
    StoredVariable<Float> result = StoredVariable.fromVariable(floatVariable);
    Assert.assertEquals(DataType.NUMBER.getName(), result.dataType);
    CompareHelper.assertEqualsVariable(result, floatVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void modifiedFloatVariableConvertsToStorageTest() {
    floatListVariable.setValue(154f);
    StoredVariable<Float> result = StoredVariable.fromVariable(floatVariable);
    Assert.assertEquals(DataType.NUMBER.getName(), result.dataType);
    CompareHelper.assertEqualsVariable(result, floatVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void rangeVariableConvertsToStorageTest() {
    StoredVariable<Float> result = StoredVariable.fromVariable(rangeVariable);
    Assert.assertEquals(DataType.NUMBER.getName(), result.dataType);
    CompareHelper.assertEqualsRangeVariable(result, rangeVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));

  }

  @Test
  public void modifiedRangeVariableConvertsToStorageTest() {
    rangeVariable.setValue(20f);
    StoredVariable<Float> result = StoredVariable.fromVariable(rangeVariable);
    Assert.assertEquals(DataType.NUMBER.getName(), result.dataType);
    CompareHelper.assertEqualsRangeVariable(result, rangeVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }
}
