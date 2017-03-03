/*
 * Copyright 2017 Google Inc.
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

package com.google.android.libraries.remixer.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.GsonProvider;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer.storage")
public class LocalStorageTest {

  Remixer remixer;
  private Variable<Boolean> booleanVariable;
  private Variable<String> stringVariable;
  private ItemListVariable<Integer> colorListVariable;
  private RangeVariable rangeVariable;
  private Gson gson;

  @BeforeClass
  public static void initRemixer() {
    Remixer.registerDataType(DataType.BOOLEAN);
    Remixer.registerDataType(DataType.COLOR);
    Remixer.registerDataType(DataType.NUMBER);
    Remixer.registerDataType(DataType.STRING);
  }

  @Before
  public void setUp() {
    gson = GsonProvider.getInstance();
    remixer = new Remixer();
    booleanVariable =
        new BooleanVariableBuilder().setContext(this).setKey("boolean").build();

    stringVariable =
        new StringVariableBuilder().setContext(this).setKey("string").build();

    colorListVariable =
        new ItemListVariable.Builder<Integer>()
            .setLimitedToValues(new Integer[]{Color.BLACK, Color.BLUE}).setDataType(DataType.COLOR)
            .setContext(this).setKey("colors").build();

    rangeVariable = new RangeVariable.Builder()
        .setMinValue(0).setMaxValue(15).setIncrement(0.5f).setContext(this).setKey("range").build();
  }


  @Test
  public void writesToStorage() {
    remixer.setSynchronizationMechanism(new LocalStorage(RuntimeEnvironment.application));
    remixer.addItem(booleanVariable);
    remixer.addItem(stringVariable);
    remixer.addItem(colorListVariable);
    remixer.addItem(rangeVariable);
    SharedPreferences preferences =
        RuntimeEnvironment.application.getSharedPreferences(
            "remixer_local_storage", Context.MODE_PRIVATE);
    compareToStored(preferences, booleanVariable);
    compareToStored(preferences, colorListVariable);
    compareToStored(preferences, stringVariable);
    compareToStored(preferences, rangeVariable);
  }

  @Test
  public void updatesValuesInStorage() {
    remixer.setSynchronizationMechanism(new LocalStorage(RuntimeEnvironment.application));
    remixer.addItem(booleanVariable);
    remixer.addItem(stringVariable);
    remixer.addItem(colorListVariable);
    remixer.addItem(rangeVariable);
    SharedPreferences preferences =
        RuntimeEnvironment.application.getSharedPreferences(
            "remixer_local_storage", Context.MODE_PRIVATE);
    booleanVariable.setValue(true);
    colorListVariable.setValue(Color.BLUE);
    stringVariable.setValue("i am a not so random string but oh well I will do");
    rangeVariable.setValue(12.5f);
    compareToStored(preferences, booleanVariable);
    compareToStored(preferences, colorListVariable);
    compareToStored(preferences, stringVariable);
    compareToStored(preferences, rangeVariable);
  }

  @Test
  public void readsValuesAlreadyStored() {
    Remixer helperInstance = new Remixer();
    helperInstance.setSynchronizationMechanism(new LocalStorage(RuntimeEnvironment.application));
    String differentValue =
        "This is a new value that has to remain when adding stringVariable to the other remixer";
    Variable<String> stringWithDifferentValue =
        new StringVariableBuilder()
            .setKey("string").setContext(differentValue).setInitialValue(differentValue).build();
    helperInstance.addItem(stringWithDifferentValue);
    // After this there should be a string variable with key string stored.
    remixer.setSynchronizationMechanism(new LocalStorage(RuntimeEnvironment.application));
    // Store the empty string variable with key string in the other instance of remixer.
    remixer.addItem(stringVariable);
    // Now stringVariable's value must be differentValue
    Assert.assertEquals(differentValue, stringVariable.getSelectedValue());
    SharedPreferences preferences =
        RuntimeEnvironment.application.getSharedPreferences(
            "remixer_local_storage", Context.MODE_PRIVATE);
    compareToStored(preferences, stringVariable);
  }

  private void compareToStored(SharedPreferences preferences, Variable<?> variable) {
    String storedJson = preferences.getString(variable.getKey(), "");
    StoredVariable<?> inMemoryData = StoredVariable.fromVariable(variable);
    StoredVariable<?> storedData = gson.fromJson(storedJson, StoredVariable.class);
    Assert.assertEquals(inMemoryData, storedData);
  }
}
