package com.google.android.libraries.remixer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BooleanRemixTest {

  @Mock
  RemixCallback<Boolean> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

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
