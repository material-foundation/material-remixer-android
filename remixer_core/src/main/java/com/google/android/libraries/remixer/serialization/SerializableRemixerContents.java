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

package com.google.android.libraries.remixer.serialization;

import com.google.android.libraries.remixer.IncompatibleRemixerItemsWithSameKeyException;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Variable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A copy of the Remixer data structure in an easily serializable form.
 *
 * <p>This representation discards any runtime details (number of variables with the same key,
 * callbacks, etc) to just focus on data. This object will be serialized to Json and back to get the
 * full status of Remixer across the entire app.
 */
public class SerializableRemixerContents {

  /**
   * Mapping from Remixer Item ID to the item's representation in Serializable format.
   *
   * <p>Notice that while {@link com.google.android.libraries.remixer.Remixer#keyMap} contains more
   * than one item per key, all of those items contain the same data (same value for variables), so
   * we only keep one of them here.
   *
   * <p>This is never used for anything other than storage and syncing. It is meant as a
   * serializable copy of {@link com.google.android.libraries.remixer.Remixer}. When adding
   * variables, the value should be synced to whatever is already stored here, or copied here if it
   * does not exist.
   */
  private Map<String, StoredVariable> keyToDataMap;

  public SerializableRemixerContents() {
    keyToDataMap = new HashMap<>();
  }

  /**
   * Adds an item to the map, converting it to a StoredVariable.
   *
   * <p>It only keeps one per key, as explained in {@link #keyToDataMap}
   */
  public void addItem(RemixerItem item) {
    addItem(StoredVariable.fromRemixerItem(item));
  }

  /**
   * Adds an item to the map.
   *
   * <p>It only keeps one per key, as explained in {@link #keyToDataMap}
   */
  public void addItem(StoredVariable item) {
    StoredVariable existingItem = keyToDataMap.get(item.key);
    if (existingItem == null) {
      keyToDataMap.put(item.key, item);
    } else if (!existingItem.isCompatibleWith(item)) {
      throw new IncompatibleRemixerItemsWithSameKeyException(
          String.format(
              Locale.getDefault(),
              "Two variables with the same key, %s, have incompatible configurations "
                  + "(data types %s, %s)",
              item.key,
              existingItem.dataType,
              item.dataType));
    }
    // If it is already there and compatible we need to do nothing here. The syncing mechanism needs
    // to sync the value across different instances though.
  }

  public Set<String> keySet() {
    return keyToDataMap.keySet();
  }

  public StoredVariable getItem(String key) {
    return keyToDataMap.get(key);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    SerializableRemixerContents serializableRemixerContents = (SerializableRemixerContents) obj;

    return keyToDataMap.equals(serializableRemixerContents.keyToDataMap);

  }

  @Override
  public int hashCode() {
    return keyToDataMap.hashCode();
  }

  /**
   * Sets the value for the StoredVariable with key {@code variable.getKey()}. Must be called only
   * after calling {@link #addItem(RemixerItem)} for a variable with this key.
   */
  @SuppressWarnings("unchecked")
  public void setValue(Variable variable) {
    StoredVariable storedVariable = StoredVariable.fromRemixerItem(variable);
    StoredVariable existingStoredVariable = keyToDataMap.get(storedVariable.key);
    if (!existingStoredVariable.isCompatibleWith(storedVariable)) {
      throw new IncompatibleRemixerItemsWithSameKeyException(
          String.format(
              Locale.getDefault(),
              "Setting value for key %s using incompatible variable. Existing data type is: %s, "
                  + "new value data type is: %s",
              storedVariable.key,
              existingStoredVariable.dataType,
              storedVariable.dataType));
    }
    existingStoredVariable.selectedValue = storedVariable.selectedValue;
  }
}
