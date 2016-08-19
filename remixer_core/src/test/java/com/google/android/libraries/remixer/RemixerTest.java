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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RemixerTest {

  private Remixer remixer;
  private Remix remix;
  private Remix remix2;

  @Before
  public void setUp() {
    remix = new RangeRemix("name", "key", 5, 0, 10, null, 0);
    remix2 = new RangeRemix("name2", "key2", 5, 0, 10, null, 0);
    remixer = new Remixer();
  }

  @Test(expected = DuplicateRemixKeyException.class)
  public void remixerRejectsDuplicates() {
    remixer.addRemix(remix);
    remixer.addRemix(remix);
  }

  @Test
  public void remixerReturnsListInOrder() {
    remixer.addRemix(remix);
    remixer.addRemix(remix2);
    List<Remix<?>> remixList = remixer.getRemixList();
    Assert.assertEquals(remix, remixList.get(0));
    Assert.assertEquals(remix2, remixList.get(1));
  }
}
