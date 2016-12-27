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

package com.google.android.libraries.remixer.sync;

import com.google.android.libraries.remixer.BooleanVariableBuilder;
import com.google.android.libraries.remixer.IncompatibleRemixerItemsWithSameKeyException;
import com.google.android.libraries.remixer.InitializationHelper;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.StringVariableBuilder;
import com.google.android.libraries.remixer.Variable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LocalValueSyncingTest {

  private Remixer remixer;
  private LocalValueSyncing localValueSyncing;

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init();
  }

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
    final Variable<String> variableString =
        new StringVariableBuilder().setKey("key").setContext(parent1).build();
    final Variable<Boolean> variableBoolean =
        new BooleanVariableBuilder().setKey("key").setContext(parent2).build();

    remixer.addItem(variableString);
    remixer.addItem(variableBoolean);
  }

  @Test(expected = IncompatibleRemixerItemsWithSameKeyException.class)
  public void rejectsDuplicatesWithDifferentTypesAfterParentObjectReclaimed() {
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString =
        new StringVariableBuilder().setKey("key").setContext(parent1).build();
    final Variable<Boolean> variableBoolean =
        new BooleanVariableBuilder().setKey("key").setContext(parent2).build();

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
    final Variable<String> variableString =
        new StringVariableBuilder().setKey("key").setContext(parent1).build();
    final Variable<String> variableString2 =
        new StringVariableBuilder().setKey("key").setContext(parent2).build();
    remixer.addItem(variableString);
    variableString.setValue("May the force be with you");
    Assert.assertEquals("May the force be with you", variableString.getSelectedValue());
    remixer.addItem(variableString2);
    Assert.assertEquals(variableString.getSelectedValue(), variableString2.getSelectedValue());
  }

  @Test
  public void acceptsVariablesWithDifferentTitlesSameKeysAndDifferentContexts() {
    // Initialize two nearly identical variables with two different parent objects of the same class
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString =
        new StringVariableBuilder().setKey("key").setTitle("SomeTitle").setContext(parent1).build();
    final Variable<String> variableString2 =
        new StringVariableBuilder().setKey("key").setContext(parent2).build();
    remixer.addItem(variableString);
    remixer.addItem(variableString2);
  }

  @Test
  public void updatesAllExistingVariableValuesWhenAnyOfThemChanges() {
    // Initialize two nearly identical variables with two different parent objects of the same class
    final Object parent1 = new Object();
    final Object parent2 = new Object();
    final Variable<String> variableString =
        new StringVariableBuilder().setKey("key").setContext(parent1).build();
    final Variable<String> variableString2 =
        new StringVariableBuilder().setKey("key").setContext(parent2).build();
    remixer.addItem(variableString);
    remixer.addItem(variableString2);
    variableString.setValue("May the force be with you");
    Assert.assertEquals("May the force be with you", variableString.getSelectedValue());
    Assert.assertEquals(variableString.getSelectedValue(), variableString2.getSelectedValue());
  }
}
