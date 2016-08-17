package com.google.android.libraries.remixer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StringRemixTest {

  @Mock
  RemixCallback<String> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void constructorCallsCallback() {
    StringRemix remix = new StringRemix("name", "key", "A", mockCallback, 0);
    Mockito.verify(mockCallback, Mockito.times(1)).onValueSet(remix);
  }

  @Test
  public void setValueCallsCallback() {
    StringRemix remix = new StringRemix("name", "key", "A", mockCallback, 0);
    remix.setValue("B");
    Mockito.verify(mockCallback, Mockito.times(2)).onValueSet(remix);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    StringRemix remix = new StringRemix("name", "key", "A", null, 0);
    remix.setValue("B");
  }
}
