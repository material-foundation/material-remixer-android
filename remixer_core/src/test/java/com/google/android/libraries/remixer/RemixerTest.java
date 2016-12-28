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

package com.google.android.libraries.remixer;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RemixerTest {

  private Remixer remixer;
  private Variable variable;
  private Variable variable2;

  @BeforeClass
  public static void oneTimeSetUp() {
    InitializationHelper.init();
  }

  /**
   * Sets up the tests.
   */
  @Before
  public void setUp() {
    variable = new StringVariableBuilder().setKey("key").setContext(this).build();
    variable.init();
    variable2 = new StringVariableBuilder().setKey("key2").setContext(this).build();
    variable2.init();
    remixer = new Remixer();
  }

  @Test(expected = DuplicateKeyException.class)
  public void remixerRejectsDuplicatesForSamecontext() {
    remixer.addItem(variable);
    remixer.addItem(variable);
  }

  /**
   * Replacement should only happen if the first context has been reclaimed and the remixer
   * item being added has a context of the same class.
   */
  @Test
  public void remixerReplacesVariableCorrectly() {
    // Initialize two nearly identical variables with two different contexts of the same class
    final Object context1 = new Object();
    final Object context2 = new Object();
    final Variable<String> variableString =
        new StringVariableBuilder().setKey("key").setContext(context1).build();
    final Variable<String> variableString2 =
        new StringVariableBuilder().setKey("key").setContext(context2).build();

    // Add the first.
    remixer.addItem(variableString);
    // Simulate the first parent object is reclaimed.
    remixer.onActivityDestroyed(context1);
    // Add the second, since the first object was "reclaimed" and they are compatible, the first
    // should be removed, and the second should be kept.
    remixer.addItem(variableString2);
    List<Variable> list1 = remixer.getVariablesWithContext(context1);
    List<Variable> list2 = remixer.getVariablesWithContext(context2);
    Assert.assertNull(list1);
    Assert.assertEquals(1, list2.size());
    Assert.assertEquals(variableString2, list2.get(0));
  }

  @Test
  public void remixerReturnsListInOrder() {
    remixer.addItem(variable);
    remixer.addItem(variable2);
    List<Variable> variableList = remixer.getVariablesWithContext(this);
    Assert.assertEquals(variable, variableList.get(0));
    Assert.assertEquals(variable2, variableList.get(1));
  }
}
