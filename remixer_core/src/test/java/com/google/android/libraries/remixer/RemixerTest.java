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
