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

package com.google.android.libraries.remixer.ui.view;

import com.google.android.libraries.remixer.BooleanRemix;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.StringRemix;
import com.google.android.libraries.remixer.ui.R;

import java.util.HashMap;
import java.util.Locale;

/**
 * Utility class to determine what widget to inflate for a remix.
 */
public final class RemixWidgetHelper {

  private RemixWidgetHelper() {}

  private static final String UNKNOWN_DEFAULT_ERROR_FORMAT =
      "Remix with key %s has no mapping to a layout resource. Cannot display it.";

  private static final HashMap<Class<?>, Integer> mapping;

  static {
    mapping = new HashMap<>();
    mapping.put(BooleanRemix.class, R.layout.boolean_remix_widget);
    mapping.put(ItemListRemix.class, R.layout.item_list_remix_widget);
    mapping.put(RangeRemix.class, R.layout.seekbar_range_remix_widget);
    mapping.put(StringRemix.class, R.layout.string_remix_widget);
  }

  /**
   * Returns the layout id to inflate for this Remix.
   *
   * <p>If no layout has been specified for the remix (it is 0), it tries to fall back to known
   * default layout ids.
   *
   * @throws IllegalArgumentException if the Remix in question has no default layout associated with
   *     it and it is relying on a default.
   */
  public static int getLayoutId(Remix<?> instance) {
    int layoutId = instance.getControlViewResourceId();
    if (layoutId != 0) {
      // This instance has a preferred layout.
      return layoutId;
    }
    if (mapping.containsKey(instance.getClass())) {
      return mapping.get(instance.getClass());
    }
    // There is no mapping, there is no layoutId whatsoever. What do we do? Throw an informative
    // exception.
    throw new IllegalArgumentException(
        String.format(Locale.getDefault(), UNKNOWN_DEFAULT_ERROR_FORMAT, instance.getKey()));
  }
}
