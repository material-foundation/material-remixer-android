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

package com.google.android.libraries.remixer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains a list of {@link Variable}es.
 */
public class Remixer {

  private HashMap<String, RemixerItem> keyMap;
  private List<RemixerItem> remixerItems;

  public Remixer() {
    keyMap = new HashMap<>();
    remixerItems = new ArrayList<>();
  }

  /**
   * This adds a remixer item ({@link Variable} or {@link Trigger}) to be tracked and displayed.
   *
   * @param remixerItem The remixer item to be added.
   * @throws DuplicateRemixerKeyException In case the remix has a key that has already been used.
   */
  public void addItem(RemixerItem remixerItem) {
    checkUniqueKey(remixerItem.getKey(), remixerItem);
    remixerItems.add(remixerItem);
  }

  private void checkUniqueKey(String key, RemixerItem remixerItem) {
    if (keyMap.containsKey(key)) {
      throw new DuplicateRemixerKeyException(
        String.format(
          "Trying to add a %s as Remixer key %s but a %s already has that key",
          remixerItem.getClass().getName(),
          key,
          keyMap.get(key).getClass().getName()));
    }
    keyMap.put(key, remixerItem);
  }

  public List<RemixerItem> getRemixerItems() {
    return remixerItems;
  }
}
