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
  Callback<Float> singleIncrementsCallback;
  @Mock
  Callback<Float> increments5Callback;

  private RangeVariable singleIncrements;
  private RangeVariable increments5;

  /**
   * Sets up the test.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    singleIncrements =
        new RangeVariable.Builder()
            .setMinValue(0f)
            .setMaxValue(20f)
            .setIncrement(1f)
            .setInitialValue(15f)
            .setCallback(singleIncrementsCallback)
            .setKey("key")
            .setContext(this)
            .build();
    increments5 =
        new RangeVariable.Builder()
            .setMinValue(0f)
            .setMaxValue(20f)
            .setIncrement(5f)
            .setInitialValue(15f)
            .setCallback(increments5Callback)
            .setKey("key")
            .setContext(this)
            .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void initDoesNotAcceptInitialValueGreaterThanMax() {
    new RangeVariable.Builder()
        .setMinValue(0f)
        .setMaxValue(10f)
        .setIncrement(5f)
        .setInitialValue(15f)
        .setKey("key")
        .setContext(this)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void initDoesNotAcceptInitialValueLessThanMin() {
    new RangeVariable.Builder()
        .setMinValue(10f)
        .setMaxValue(20f)
        .setIncrement(5f)
        .setInitialValue(5f)
        .setKey("key")
        .setContext(this)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidRanges() {
    new RangeVariable.Builder()
        .setMinValue(50f)
        .setMaxValue(20f)
        .setIncrement(1f)
        .setInitialValue(15f)
        .setKey("key")
        .setContext(this)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptNegativeStepping() {
    new RangeVariable.Builder()
        .setMinValue(0f)
        .setMaxValue(20f)
        .setIncrement(-1f)
        .setInitialValue(15f)
        .setKey("key")
        .setContext(this)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidStepping() {
    // Stepping is invalid because maxValue 52 cannot be reached from 15 in steps of 5
    new RangeVariable.Builder()
        .setMinValue(0f)
        .setMaxValue(52f)
        .setIncrement(5f)
        .setInitialValue(15f)
        .setKey("key")
        .setContext(this)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void initDoesNotAcceptInvalidSteppingToInitialValue() {
    // Stepping is invalid because initialValue 22 cannot be reached from 15 in steps of 5
    new RangeVariable.Builder()
        .setMinValue(0f)
        .setMaxValue(50f)
        .setIncrement(5f)
        .setInitialValue(22f)
        .setKey("key")
        .setContext(this)
        .build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueLessThanMin() {
    singleIncrements.setValue(-1f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueGreaterThanMax() {
    singleIncrements.setValue(100f);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsInvalidValueForStepping() {
    increments5.setValue(12f);
  }

  @Test
  public void callbackIsCalledOnInit() {
    Mockito.verify(singleIncrementsCallback, Mockito.times(1)).onValueSet(singleIncrements);
  }

  @Test
  public void callbackIsCalledAfterValueSet() {
    singleIncrements.setValue(18f);
    Mockito.verify(singleIncrementsCallback, Mockito.times(2)).onValueSet(singleIncrements);
    increments5.setValue(5f);
    Mockito.verify(increments5Callback, Mockito.times(2)).onValueSet(increments5);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    RangeVariable variable =
        new RangeVariable.Builder()
            .setMinValue(0f)
            .setMaxValue(20f)
            .setIncrement(1f)
            .setInitialValue(15f)
            .setKey("key")
            .setContext(this)
            .build();
    variable.setValue(18f);
  }
}
