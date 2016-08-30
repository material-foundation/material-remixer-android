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

package com.google.android.libraries.remixer.ui.widget;

import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.ui.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer.ui")
public class SeekBarRangeRemixWidgetTest {
  private static final String TITLE = "Padding for buttons in dp";
  private static final String KEY = "button_padding";
  private static final int MIN = 4;
  private static final int MAX = 20;
  private static final int DEFAULT_VALUE = 8;
  private static final int STEPPING = 1;

  @Mock
  RemixCallback<Integer> mockCallback;

  private RangeRemix remix;
  private SeekBarRangeRemixWidget view;
  private TextView name;
  private TextView currentValue;
  private SeekBar seekbar;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    remix = new RangeRemix(
        TITLE,
        KEY,
        DEFAULT_VALUE,
        MIN,
        MAX,
        STEPPING,
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
