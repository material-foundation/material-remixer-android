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

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.ui.R;
import com.google.android.libraries.remixer.ui.widget.color.ColorItem;
import java.util.Arrays;
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
public class ColorListRemixWidgetTest {
  private static final String TITLE = "Color";
  private static final String KEY = "color";
  private static final Integer[] ITEM_LIST = new Integer[]{
      Color.BLACK,
      Color.BLUE,
      Color.RED,
  };
  private static final int DEFAULT_VALUE_INDEX = 2;

  @Mock
  RemixCallback<Integer> mockCallback;

  private ItemListRemix<Integer> remix;
  private TextView name;
  private ColorListRemixWidget.Adapter adapter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    remix = new ItemListRemix<Integer>(
        TITLE,
        KEY,
        ITEM_LIST[DEFAULT_VALUE_INDEX],
        Arrays.asList(ITEM_LIST),
        mockCallback,
        R.layout.item_list_remix_widget);
    remix.init();
    ColorListRemixWidget view =
        (ColorListRemixWidget) LayoutInflater.from(RuntimeEnvironment.application)
            .inflate(R.layout.color_list_remix_widget, null);
    view.bindRemixerItem(remix);
    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.colorList);
    name = (TextView) view.findViewById(R.id.remixName);
    adapter = (ColorListRemixWidget.Adapter) recyclerView.getAdapter();
  }

  private ColorItem getSelectedItem() {
    for (ColorItem item : adapter.getValues()) {
      if (item.isSelected()) {
        return item;
      }
    }
    return null;
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(ITEM_LIST[DEFAULT_VALUE_INDEX].intValue(), getSelectedItem().getColor());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(remix);
    // Simulate a new selection.
    adapter.selectColor(ITEM_LIST[0]);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[0].intValue(), getSelectedItem().getColor());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(remix);
    adapter.selectColor(ITEM_LIST[1]);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[1].intValue(), getSelectedItem().getColor());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(3)).onValueSet(remix);
  }
}
