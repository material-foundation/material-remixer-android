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

import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import org.junit.Assert;

public class CompareHelper {

  public static void assertEqualsTrigger(StoredVariable storage, Trigger trigger) {
    assertConsistent(storage, trigger);
    Assert.assertNull(storage.selectedValue);
    Assert.assertNull(storage.possibleValues);
    Assert.assertNull(storage.minValue);
    Assert.assertNull(storage.maxValue);
    Assert.assertNull(storage.increment);
  }

  public static <T> void assertEqualsVariable(StoredVariable<T> storage, Variable<T> variable) {
    assertConsistent(storage, variable);
    Assert.assertEquals(variable.getSelectedValue(), storage.selectedValue);
    Assert.assertNull(storage.possibleValues);
    Assert.assertNull(storage.minValue);
    Assert.assertNull(storage.maxValue);
    Assert.assertNull(storage.increment);
  }

  public static <T> void assertEqualsItemListVariable(
      StoredVariable<T> storage, ItemListVariable<T> variable) {
    assertConsistent(storage, variable);
    Assert.assertEquals(variable.getSelectedValue(), storage.selectedValue);
    Assert.assertEquals(variable.getValueList(), storage.possibleValues);
    Assert.assertNull(storage.minValue);
    Assert.assertNull(storage.maxValue);
    Assert.assertNull(storage.increment);
  }

  public static <T extends Number> void assertEqualsRangeVariable(
      StoredVariable<T> storage, RangeVariable variable) {
    assertConsistent(storage, variable);
    Assert.assertEquals(variable.getSelectedValue(), storage.selectedValue);
    Assert.assertNull(storage.possibleValues);
    Assert.assertEquals(variable.getMinValue(), storage.minValue);
    Assert.assertEquals(variable.getMaxValue(), storage.maxValue);
    Assert.assertEquals(variable.getIncrement(), storage.increment);
  }

  private static void assertConsistent(StoredVariable storage, RemixerItem item) {
    Assert.assertEquals(item.getKey(), storage.key);
    Assert.assertEquals(item.getTitle(), storage.title);
  }
}
