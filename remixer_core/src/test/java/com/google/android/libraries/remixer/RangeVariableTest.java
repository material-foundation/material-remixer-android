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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class RangeVariableTest {

  @Mock
  Callback<Integer> singleIncrementsCallback;
  @Mock
  Callback<Integer> increments5Callback;

  RangeVariable singleIncrements;
  RangeVariable increments5;

  /**
   * Sets up the test.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    singleIncrements = new RangeVariable(
        "name", "key", 15, 0, 20, 1, this, singleIncrementsCallback, 0);
    singleIncrements.init();
    increments5 = new RangeVariable("name", "key", 15, 0, 20, 5, this, increments5Callback, 0);
    increments5.init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void initDoesNotAcceptDefaultValueGreaterThanMax() {
    new RangeVariable("name", "key", 15, 0, 10, 1, this, null, 0).init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void initDoesNotAcceptDefaultValueLessThanMin() {
    new RangeVariable("name", "key", 15, 20, 30, 1, this, null, 0).init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidRanges() {
    new RangeVariable("name", "key", 15, 50, 20, 1, this, null, 0).init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptNegativeStepping() {
    new RangeVariable("name", "key", 15, 50, 20, -1, this, null, 0).init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidStepping() {
    // Stepping is invalid because maxValue 52 cannot be reached from 15 in steps of 5
    new RangeVariable("name", "key", 15, 0, 52, 5, this, null, 0).init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void initDoesNotAcceptInvalidSteppingToDefaultValue() {
    // Stepping is invalid because defaultValue 22 cannot be reached from 15 in steps of 5
    new RangeVariable("name", "key", 22, 0, 50, 5, this, null, 0).init();
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueLessThanMin() {
    singleIncrements.setValue(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueGreaterThanMax() {
    singleIncrements.setValue(100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsInvalidValueForStepping() {
    increments5.setValue(12);
  }

  @Test
  public void callbackIsCalledOnInit() {
    Mockito.verify(singleIncrementsCallback, Mockito.times(1)).onValueSet(singleIncrements);
  }

  @Test
  public void callbackIsCalledAfterValueSet() {
    singleIncrements.setValue(18);
    Mockito.verify(singleIncrementsCallback, Mockito.times(2)).onValueSet(singleIncrements);
    increments5.setValue(5);
    Mockito.verify(increments5Callback, Mockito.times(2)).onValueSet(increments5);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    RangeVariable variable = new RangeVariable("name", "key", 15, 0, 20, 1, this, null, 0);
    variable.init();
    variable.setValue(18);
  }
}
