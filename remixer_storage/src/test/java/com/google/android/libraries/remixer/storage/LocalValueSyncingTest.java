/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.storage;

import com.google.android.libraries.remixer.IncompatibleRemixerItemsWithSameKeyException;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;
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
public class LocalValueSyncingTest {

  private Remixer remixer;
  private LocalValueSyncing localValueSyncing;

  @Before
  public void setUp() {
    remixer = new Remixer();
    localValueSyncing = new LocalValueSyncing();
    remixer.setSynchronizationMechanism(localValueSyncing);
  }

  @Test(expected = IncompatibleRemixerItemsWithSameKeyException.class)
  public void rejectsDuplicatesWithDifferentTypes() {
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", parent1, null, 0);
    final Variable<Boolean> variableBoolean =
        new Variable<>("name", "key", false, parent2, null, 0);

    remixer.addItem(variableString);
    remixer.addItem(variableBoolean);
  }

  @Test(expected = IncompatibleRemixerItemsWithSameKeyException.class)
  public void rejectsDuplicatesOneVariableOneTrigger() {
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", parent1, null, 0);
    final Trigger trigger = new Trigger("name", "key", parent2, null, 0);

    remixer.addItem(variableString);
    remixer.addItem(trigger);
  }

  @Test(expected = IncompatibleRemixerItemsWithSameKeyException.class)
  public void rejectsDuplicatesOneVariableOneTriggerAfterParentObjectReclaimed() {
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", parent1, null, 0);
    final Trigger trigger = new Trigger("name", "key", parent2, null, 0);

    remixer.addItem(variableString);
    // Simulate parent object was reclaimed for variableString
    remixer.onActivityDestroyed(parent1);
    remixer.addItem(trigger);
  }

  @Test(expected = IncompatibleRemixerItemsWithSameKeyException.class)
  public void rejectsDuplicatesWithDifferentTypesAfterParentObjectReclaimed() {
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", parent1, null, 0);
    final Variable<Boolean> variableBoolean =
        new Variable<>("name", "key", false, parent2, null, 0);

    remixer.addItem(variableString);
    // Simulate parent object was reclaimed for variableString
    remixer.onActivityDestroyed(parent1);
    remixer.addItem(variableBoolean);
  }

  @Test
  public void updatesVariableValueWhenJustAdded() {
    // Initialize two nearly identical variables with two different parent objects of the same class
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", parent1, null, 0);
    final Variable<String> variableString2 = new Variable<>("name", "key", "", parent2, null, 0);
    remixer.addItem(variableString);
    variableString.setValue("May the force be with you");
    Assert.assertEquals("May the force be with you", variableString.getSelectedValue());
    remixer.addItem(variableString2);
    Assert.assertEquals(variableString.getSelectedValue(), variableString2.getSelectedValue());
  }

  @Test
  public void updatesAllExistingVariableValuesWhenAnyOfThemChanges() {
    // Initialize two nearly identical variables with two different parent objects of the same class
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", parent1, null, 0);
    final Variable<String> variableString2 = new Variable<>("name", "key", "", parent2, null, 0);
    remixer.addItem(variableString);
    remixer.addItem(variableString2);
    variableString.setValue("May the force be with you");
    Assert.assertEquals("May the force be with you", variableString.getSelectedValue());
    Assert.assertEquals(variableString.getSelectedValue(), variableString2.getSelectedValue());
  }
}
