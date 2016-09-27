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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.view.LayoutInflater;
import android.widget.Button;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.ui.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer.ui")
public class TriggerWidgetTest {
  private static final String TITLE = "Trigger";
  private static final String KEY = "trigger";

  @Mock
  Runnable mockCallback;

  private Button button;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Trigger trigger = new Trigger(
        TITLE,
        KEY,
        mockCallback,
        R.layout.string_variable_widget);
    TriggerWidget view = (TriggerWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.trigger_widget, null);
    view.bindRemixerItem(trigger);
    button = (Button) view.findViewById(R.id.triggerButton);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, button.getText().toString());
  }

  @Test
  public void callbackIsCalled() {
    button.callOnClick();
    // After clicking, check that the callback was called.
    verify(mockCallback, times(1)).run();
  }
}
