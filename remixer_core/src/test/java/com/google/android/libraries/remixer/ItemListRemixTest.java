package com.google.android.libraries.remixer;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ItemListRemixTest {

  @Mock
  RemixCallback<String> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

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
