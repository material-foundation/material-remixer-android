package com.google.android.libraries.remixer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RangeRemixTest {

  @Mock
  RemixCallback<Integer> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptDefaultValueGreaterThanMax() {
    new RangeRemix("name", "key", 15, 0, 10, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptDefaultValueLessThanMin() {
    new RangeRemix("name", "key", 15, 20, 30, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidRanges() {
    new RangeRemix("name", "key", 15, 50, 10, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueLessThanMin() {
    RangeRemix remix = new RangeRemix("name", "key", 15, 0, 20, null, 0);
    remix.setValue(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueGreaterThanMax() {
    RangeRemix remix = new RangeRemix("name", "key", 15, 0, 20, null, 0);
    remix.setValue(100);
  }

  @Test
  public void callbackIsCalledOnConstructor() {
    RangeRemix remix = new RangeRemix("name", "key", 15, 0, 20, mockCallback, 0);
    Mockito.verify(mockCallback, Mockito.times(1)).onValueSet(remix);
  }

  @Test
  public void callbackIsCalledAfterValueSet() {
    RangeRemix remix = new RangeRemix("name", "key", 15, 0, 20, mockCallback, 0);
    remix.setValue(18);
    Mockito.verify(mockCallback, Mockito.times(2)).onValueSet(remix);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    RangeRemix remix = new RangeRemix("name", "key", 15, 0, 20, mockCallback, 0);
    remix.setValue(18);
  }
}
