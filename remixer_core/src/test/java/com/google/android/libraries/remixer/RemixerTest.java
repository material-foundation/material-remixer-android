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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RemixerTest {

  private Remixer remixer;
  private Variable variable;
  private Variable variable2;

  /**
   * Sets up the tests.
   */
  @Before
  public void setUp() {
    variable = new Variable<>("name", "key", "", this, null, 0);
    variable.init();
    variable2 = new Variable<>("name2", "key2", "", this, null, 0);
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
  public void remixerReplacesVariableCorrectly() {
    // Initialize two nearly identical variables with two different contexts of the same class
    final Object context1 = new Object();
    final Object context2 = new Object();
    final Variable<String> variableString = new Variable<>("name", "key", "", context1, null, 0);
    final Variable<String> variableString2 = new Variable<>("name", "key", "", context2, null, 0);

    // Add the first.
    remixer.addItem(variableString);
    // Simulate the first parent object is reclaimed.
    remixer.onActivityDestroyed(context1);
    // Add the second, since the first object was "reclaimed" and they are compatible, the first
    // should be removed, and the second should be kept.
    remixer.addItem(variableString2);
    List<RemixerItem> list1 = remixer.getRemixerItemsForContext(context1);
    List<RemixerItem> list2 = remixer.getRemixerItemsForContext(context2);
    Assert.assertEquals(0, list1.size());
    Assert.assertEquals(1, list2.size());
    Assert.assertEquals(variable2, list2.get(0));
  }

  @Test
  public void remixerReturnsListInOrder() {
    remixer.addItem(variable);
    remixer.addItem(variable2);
    List<RemixerItem> remixerItemList = remixer.getRemixerItemsForContext(this);
    Assert.assertEquals(variable, remixerItemList.get(0));
    Assert.assertEquals(variable2, remixerItemList.get(1));
  }
}
