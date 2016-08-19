package com.google.android.libraries.remixer.widget;

import android.view.LayoutInflater;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.libraries.remixer.BooleanRemix;
import com.google.android.libraries.remixer.BuildConfig;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(
    constants = BuildConfig.class,
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer")
public class BooleanRemixWidgetTest {
  private static final String TITLE = "Some boolean";
  private static final String KEY = "theboolean";
  private static final boolean DEFAULT_VALUE = false;

  @Mock
  RemixCallback<Boolean> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  private BooleanRemix remix;
  private BooleanRemixWidget view;
  private TextView name;
  private Switch remixSwitch;

  @Before
  public void setUp() {
    remix = new BooleanRemix(
        TITLE,
        KEY,
        DEFAULT_VALUE,
        mockCallback,
        R.layout.boolean_remix_widget);
    view = (BooleanRemixWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.boolean_remix_widget, null);
    view.bindRemix(remix);
    remixSwitch = (Switch) view.findViewById(R.id.booleanRemixSwitch);
    name = (TextView) view.findViewById(R.id.booleanRemixName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(DEFAULT_VALUE, remixSwitch.isChecked());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(remix);
    remixSwitch.toggle();
    // After changing the text, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(remix);
  }
}
