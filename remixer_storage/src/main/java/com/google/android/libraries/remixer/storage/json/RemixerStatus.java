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

package com.google.android.libraries.remixer.storage.json;

import com.google.android.libraries.remixer.RemixerItem;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class RemixerStatus {
  Map<String, StoredVariable> map;

  public RemixerStatus() {
    map = new HashMap<>();
  }

  public void addItem(RemixerItem item) {
    map.put(item.getKey(), StoredVariable.fromRemixerItem(item));
  }

  void addItem(StoredVariable item) {
    map.put(item.key, item);
  }

  Set<String> keySet() {
    return map.keySet();
  }

  StoredVariable getItem(String key) {
    return map.get(key);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RemixerStatus status = (RemixerStatus) o;

    return map.equals(status.map);

  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }
}
