package com.google.android.libraries.remixer.widget;

import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.libraries.remixer.BuildConfig;
import com.google.android.libraries.remixer.R;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.RangeRemix;

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
public class SeekBarRangeRemixWidgetTest {
  private static final String TITLE = "Padding for buttons in dp";
  private static final String KEY = "button_padding";
  private static final int MIN = 4;
  private static final int MAX = 20;
  private static final int DEFAULT_VALUE = 8;

  @Mock
  RemixCallback<Integer> mockCallback;

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  private RangeRemix remix;
  private SeekBarRangeRemixWidget view;
  private TextView name;
  private TextView currentValue;
  private SeekBar seekbar;

  @Before
  public void setUp() {
    remix = new RangeRemix(
        TITLE,
        KEY,
        DEFAULT_VALUE,
        MIN,
        MAX,
        mockCallback,
        R.layout.seekbar_range_remix_widget);
    view = (SeekBarRangeRemixWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.seekbar_range_remix_widget, null);
    view.bindRemix(remix);
    seekbar = (SeekBar) view.findViewById(R.id.rangeRemixSeekBar);
    currentValue = (TextView) view.findViewById(R.id.rangeRemixCurrentValue);
    name = (TextView) view.findViewById(R.id.rangeRemixName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(Integer.toString(DEFAULT_VALUE), currentValue.getText().toString());
    assertEquals(DEFAULT_VALUE - MIN, seekbar.getProgress());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(remix);
    seekbar.setProgress(0);
    // Check the currentValue displayed changes on the UI
    assertEquals(Integer.toString(MIN), currentValue.getText().toString());
    // After moving the slider, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(remix);
  }
}
