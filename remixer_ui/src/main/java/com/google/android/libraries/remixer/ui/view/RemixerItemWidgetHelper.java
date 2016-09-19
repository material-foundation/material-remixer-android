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

import android.support.annotation.Nullable;
import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.ui.R;
import java.util.HashMap;
import java.util.Locale;

/**
 * Utility class to determine what widget to inflate for a remixer item.
 */
public final class RemixerItemWidgetHelper {

  private RemixerItemWidgetHelper() {}

  private static final String UNKNOWN_DEFAULT_ERROR_FORMAT =
      "Remix with key %s has no mapping to a layout resource. Cannot display it.";

  private static final HashMap<String, Integer> mapping;

  private static String getKey(Class remixClass, @Nullable Class remixType) {
    String result = remixClass.getCanonicalName();
    if (remixType != null) {
      result = String.format(Locale.getDefault(),  "%s,%s", result, remixType.getCanonicalName());
    }
    return result;
  }

  static {
    mapping = new HashMap<>();
    mapping.put(getKey(Remix.class, Boolean.class), R.layout.boolean_remix_widget);
    mapping.put(getKey(ItemListRemix.class, null), R.layout.item_list_remix_widget);
    mapping.put(getKey(RangeRemix.class, null), R.layout.seekbar_range_remix_widget);
    mapping.put(getKey(Remix.class, String.class), R.layout.string_remix_widget);
    mapping.put(getKey(Trigger.class, null), R.layout.trigger_widget);
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
  public static int getLayoutId(RemixerItem instance) {
    int layoutId = instance.getLayoutId();
    if (layoutId != 0) {
      // This instance has a preferred layout.
      return layoutId;
    }
    String key;
    if (instance instanceof Remix) {
      Remix remix = (Remix) instance;
      key = getKey(remix.getClass(), remix.getRemixType());
      if (mapping.containsKey(key)) {
        return mapping.get(key);
      }
    }
    key = getKey(instance.getClass(), null);
    if (mapping.containsKey(key)) {
      return mapping.get(key);
    }
    // There is no mapping, there is no layoutId whatsoever. What do we do? Throw an informative
    // exception.
    throw new IllegalArgumentException(
        String.format(Locale.getDefault(), UNKNOWN_DEFAULT_ERROR_FORMAT, instance.getKey()));
  }
}
