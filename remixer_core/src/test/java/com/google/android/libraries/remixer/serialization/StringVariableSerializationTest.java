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
public class StringVariableSerializationTest {

  private static String KEY = "key";

  private Variable<String> stringVariable;
  private ItemListVariable<String> stringListVariable;
  private Gson gson = GsonProvider.getInstance();

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init();
  }

  @Before
  public void setUp() {
    stringVariable = new StringVariableBuilder()
        .setContext(this)
        .setKey(KEY)
        .build();
    stringListVariable = new ItemListVariable.Builder<String>()
        .setLimitedToValues(new String[]{"a", "b", "c"})
        .setInitialValue("a")
        .setContext(this)
        .setKey(KEY)
        .setDataType(DataType.STRING)
        .build();
  }

  @Test
  public void stringVariableConvertsToStorageTest() {
    StoredVariable<String> result = StoredVariable.fromVariable(stringVariable);
    Assert.assertEquals(DataType.STRING.getName(), result.dataType);
    CompareHelper.assertEqualsVariable(result, stringVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }

  @Test
  public void modifiedStringVariableConvertsToStorageTest() {
    stringVariable.setValue("heythere");
    StoredVariable<String> result = StoredVariable.fromVariable(stringVariable);
    Assert.assertEquals(DataType.STRING.getName(), result.dataType);
    CompareHelper.assertEqualsVariable(result, stringVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }

  @Test
  public void stringListVariableConvertsToStorageTest() {
    StoredVariable<String> result = StoredVariable.fromVariable(stringListVariable);
    Assert.assertEquals(DataType.STRING.getName(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, stringListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }

  @Test
  public void modifiedStringListariableConvertsToStorageTest() {
    stringListVariable.setValue("b");
    StoredVariable<String> result = StoredVariable.fromVariable(stringListVariable);
    Assert.assertEquals(DataType.STRING.getName(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, stringListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }
}
