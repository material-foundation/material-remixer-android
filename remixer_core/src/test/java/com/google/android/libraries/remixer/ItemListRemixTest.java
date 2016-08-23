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

import java.util.Arrays;

@RunWith(JUnit4.class)
public class ItemListRemixTest {

  @Mock
  RemixCallback<String> mockCallback;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }
  @Test(expected = IllegalArgumentException.class)
  public void failsOnDefaultValueNotInList() {
    ItemListRemix<String> remix =
        new ItemListRemix<String>("name", "key", "None", Arrays.asList("Something else"), null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsUnknownString() {
    ItemListRemix<String> remix =
        new ItemListRemix<String>("name", "key", "A", Arrays.asList("A", "B"), null, 0);
    remix.setValue("C");
  }

  @Test
  public void constructorCallsCallback() {
    ItemListRemix<String> remix =
        new ItemListRemix<String>("name", "key", "A", Arrays.asList("A", "B"), mockCallback, 0);
    Mockito.verify(mockCallback, Mockito.times(1)).onValueSet(remix);
  }

  @Test
  public void setValueCallsCallback() {
    ItemListRemix<String> remix =
        new ItemListRemix<String>("name", "key", "A", Arrays.asList("A", "B"), mockCallback, 0);
    remix.setValue("B");
    Mockito.verify(mockCallback, Mockito.times(2)).onValueSet(remix);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    ItemListRemix<String> remix =
        new ItemListRemix<String>("name", "key", "A", Arrays.asList("A", "B"), null, 0);
    remix.setValue("B");
  }
}
