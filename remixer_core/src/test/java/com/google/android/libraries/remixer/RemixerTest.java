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
  private Remix remix;
  private Remix remix2;

  /**
   * Sets up the tests.
   */
  @Before
  public void setUp() {
    remix = new Remix<>("name", "key", "", null, 0);
    remix.init();
    remix2 = new Remix<>("name2", "key2", "", null, 0);
    remix2.init();
    remixer = new Remixer();
  }

  @Test(expected = DuplicateRemixKeyException.class)
  public void remixerRejectsDuplicates() {
    remixer.addItem(remix);
    remixer.addItem(remix);
  }

  @Test
  public void remixerReturnsListInOrder() {
    remixer.addItem(remix);
    remixer.addItem(remix2);
    List<RemixerItem> remixList = remixer.getRemixerItems();
    Assert.assertEquals(remix, remixList.get(0));
    Assert.assertEquals(remix2, remixList.get(1));
  }
}
