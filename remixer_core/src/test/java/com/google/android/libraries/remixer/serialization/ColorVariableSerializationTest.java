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
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ColorVariableSerializationTest {

  protected static final int BLACK = 0x00000000;
  protected static final int BLUE = 0xFF0000FF;
  private static String KEY = "key";

  private ItemListVariable<Integer> colorListVariable;
  private Gson gson = GsonProvider.getInstance();

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init();
  }

  @Before
  public void setUp() {
    colorListVariable = new ItemListVariable.Builder<Integer>()
        .setLimitedToValues(new Integer[]{BLACK, BLUE})
        .setInitialValue(BLACK)
        .setContext(this)
        .setKey(KEY)
        .setDataType(DataType.COLOR)
        .build();
  }

  @Test
  public void colorListVariableConvertsToStorageTest() {
    StoredVariable<Integer> result = StoredVariable.fromVariable(colorListVariable);
    Assert.assertEquals(DataType.COLOR.getName(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, colorListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }

  @Test
  public void modifiedColorListVariableConvertsToStorageTest() {
    colorListVariable.setValue(BLUE);
    StoredVariable<Integer> result = StoredVariable.fromVariable(colorListVariable);
    Assert.assertEquals(DataType.COLOR.getName(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, colorListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }
}
