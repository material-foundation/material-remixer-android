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
public class StringVariableTest {

  @Mock
  Callback<String> mockCallback;
  private Variable<String> variableWithCallback;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    variableWithCallback = new StringVariableBuilder()
        .setKey("key").setContext(this).setCallback(mockCallback).build();
  }

  @Test
  public void initCallsCallback() {
    Mockito.verify(mockCallback, Mockito.times(1)).onValueSet(variableWithCallback);
  }

  @Test
  public void setValueCallsCallback() {
    variableWithCallback.setValue("B");
    Mockito.verify(mockCallback, Mockito.times(2)).onValueSet(variableWithCallback);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    Variable<String> variable =
        new StringVariableBuilder().setKey("key").setContext(this).build();
    variable.init();
    variable.setValue("B");
  }
}
