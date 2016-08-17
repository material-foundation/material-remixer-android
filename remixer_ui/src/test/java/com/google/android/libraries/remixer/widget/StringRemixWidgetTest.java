package com.google.android.libraries.remixer.widget;

import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.libraries.remixer.BuildConfig;
import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.StringRemix;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class,
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer")
public class StringRemixWidgetTest {
  private static final String TITLE = "Color";
  private static final String KEY = "color";
  private static final String DEFAULT_VALUE = "red";

  @Mock
  RemixCallback<String> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  private StringRemix remix;
  private StringRemixWidget view;
  private TextView name;
  private EditText text;

  @Before
  public void setUp() {
    remix = new StringRemix(
        TITLE,
        KEY,
        DEFAULT_VALUE,
        mockCallback,
        R.layout.string_remix_widget);
    view = (StringRemixWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.string_remix_widget, null);
    view.bindRemix(remix);
    text = (EditText) view.findViewById(R.id.stringRemixText);
    name = (TextView) view.findViewById(R.id.stringRemixName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(DEFAULT_VALUE, text.getText().toString());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(remix);
    text.setText("green");
    // After changing the text, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(remix);
  }
}
