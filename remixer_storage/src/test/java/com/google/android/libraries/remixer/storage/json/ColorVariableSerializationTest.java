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
import com.google.android.libraries.remixer.ItemListVariable;
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
public class ColorVariableSerializationTest {

  private static String KEY = "key";

  private ItemListVariable<Integer> colorListVariable;
  private Gson gson = GsonProvider.getInstance();

  @Before
  public void setUp() {
    colorListVariable = new ItemListVariable.Builder<Integer>()
        .setPossibleValues(new Integer[]{Color.BLACK, Color.BLUE})
        .setDefaultValue(Color.BLACK)
        .setContext(this)
        .setKey(KEY)
        .setLayoutId(com.google.android.libraries.remixer.ui.R.layout.color_list_variable_widget)
        .build();
  }

  @Test
  public void colorListVariableConvertsToStorageTest() {
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(colorListVariable);
    Assert.assertEquals(SupportedDataType.COLOR.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, colorListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }

  @Test
  public void modifiedColorListVariableConvertsToStorageTest() {
    colorListVariable.setValue(Color.BLUE);
    StoredVariable<Integer> result = StoredVariable.fromRemixerItem(colorListVariable);
    Assert.assertEquals(SupportedDataType.COLOR.getDataTypeSerializableString(), result.dataType);
    CompareHelper.assertEqualsItemListVariable(result, colorListVariable);
    // Check that it converts to Json and back with no data loss.
    Assert.assertEquals(result, gson.fromJson(gson.toJsonTree(result), StoredVariable.class));
  }
}
