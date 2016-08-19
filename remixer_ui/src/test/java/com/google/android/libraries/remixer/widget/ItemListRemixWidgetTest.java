package com.google.android.libraries.remixer.widget;

import android.view.LayoutInflater;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.libraries.remixer.BuildConfig;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.RemixCallback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class,
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer")
public class ItemListRemixWidgetTest {
  private static final String TITLE = "Color";
  private static final String KEY = "color";
  private static final String[] ITEM_LIST = new String[]{
      "red",
      "blue",
      "yellow",
      "green"
  };
  private static final int DEFAULT_VALUE_INDEX = 2;

  @Mock
  RemixCallback<String> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  private ItemListRemix<String> remix;
  private ItemListRemixWidget view;
  private TextView name;
  private Spinner spinner;

  @Before
  public void setUp() {
    remix = new ItemListRemix<String>(
        TITLE,
        KEY,
        ITEM_LIST[DEFAULT_VALUE_INDEX],
        Arrays.asList(ITEM_LIST),
        mockCallback,
        R.layout.item_list_remix_widget);
    view = (ItemListRemixWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.item_list_remix_widget, null);
    view.bindRemix(remix);
    spinner = (Spinner) view.findViewById(R.id.itemListRemixSpinner);
    name = (TextView) view.findViewById(R.id.itemListRemixName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(ITEM_LIST[DEFAULT_VALUE_INDEX], spinner.getSelectedItem());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(remix);
    spinner.setSelection(0);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[0], spinner.getSelectedItem());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(remix);
    spinner.setSelection(1);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[1], spinner.getSelectedItem());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(3)).onValueSet(remix);
  }
}
