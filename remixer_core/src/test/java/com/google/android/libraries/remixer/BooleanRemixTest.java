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
public class BooleanRemixTest {

  @Mock
  RemixCallback<Boolean> mockCallback;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void constructorCallsCallback() {
    BooleanRemix remix = new BooleanRemix("name", "key", false, mockCallback, 0);
    Mockito.verify(mockCallback, Mockito.times(1)).onValueSet(remix);
  }

  @Test
  public void setValueCallsCallback() {
    BooleanRemix remix = new BooleanRemix("name", "key", false, mockCallback, 0);
    remix.setValue(true);
    Mockito.verify(mockCallback, Mockito.times(2)).onValueSet(remix);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    BooleanRemix remix = new BooleanRemix("name", "key", false, null, 0);
    remix.setValue(true);
  }
}
