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

import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.RemixerItem;
import java.util.List;

/**
 * This is an intermediate format that conforms to the agreed upon cross-platform storage and sync
 * protocol.
 *
 * <p>This is never used for anything other than storage and syncing and is meant to be converted to
 * a regular {@link com.google.android.libraries.remixer.RemixerItem} as soon as it's completely
 * parsed.
 */
class StoredVariable<T> {

  // Json dictionary keys to serialize this object
  static final String KEY = "key";
  static final String TITLE = "title";
  static final String DATA_TYPE = "dataType";
  static final String SELECTED_VALUE = "selectedValue";
  static final String POSSIBLE_VALUES = "possibleValues";
  static final String MIN_VALUE = "minValue";
  static final String MAX_VALUE = "maxValue";
  static final String INCREMENT = "increment";

  // This first section applies to every Remixer item.
  /**
   * The RemixerItem's key.
   */
  String key;
  /**
   * The RemixerItem's title. It's optional.
   */
  String title;
  /**
   * The data type this variable represents, it's the string representation of one of the {@link
   * SupportedDataType}.
   */
  String dataType;

  // From here, this only applies to Variables, not triggers.
  /**
   * The currently selected value for the variable.
   */
  T selectedValue;
  /**
   * The list of possible values, if this is not null then this StoredVariable represents a {@link
   * com.google.android.libraries.remixer.ItemListVariable}.
   */
  List<T> possibleValues;

  // These are only used in case it T is a number and it is a range variable, otherwise they are
  // ignored.
  /**
   * The minimum value for the {@link com.google.android.libraries.remixer.RangeVariable}
   */
  T minValue;
  /**
   * The maximum value for the {@link com.google.android.libraries.remixer.RangeVariable}
   */
  T maxValue;
  /**
   * The increment value for the {@link com.google.android.libraries.remixer.RangeVariable}
   */
  T increment;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StoredVariable<?> variable = (StoredVariable<?>) o;

    if (!key.equals(variable.key)) return false;
    if (title != null ? !title.equals(variable.title) : variable.title != null) return false;
    if (!dataType.equals(variable.dataType)) return false;
    if (selectedValue != null ?
        !selectedValue.equals(variable.selectedValue) : variable.selectedValue != null)
      return false;
    if (possibleValues != null ?
        !possibleValues.equals(variable.possibleValues) : variable.possibleValues != null)
      return false;
    if (minValue != null ? !minValue.equals(variable.minValue) : variable.minValue != null)
      return false;
    if (maxValue != null ? !maxValue.equals(variable.maxValue) : variable.maxValue != null)
      return false;
    return increment != null ? increment.equals(variable.increment) : variable.increment == null;
  }

  @Override
  public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + dataType.hashCode();
    result = 31 * result + (selectedValue != null ? selectedValue.hashCode() : 0);
    result = 31 * result + (possibleValues != null ? possibleValues.hashCode() : 0);
    result = 31 * result + (minValue != null ? minValue.hashCode() : 0);
    result = 31 * result + (maxValue != null ? maxValue.hashCode() : 0);
    result = 31 * result + (increment != null ? increment.hashCode() : 0);
    return result;
  }

  /**
   * Creates a Stored variable from a existing RemixerItem.
   */
  static StoredVariable fromRemixerItem(RemixerItem item) {
    StoredVariable storage = null;
    for (SupportedDataType type : SupportedDataType.values()) {
      try {
        storage = type.getValueConverter().fromRemixerItem(item);
        break;
      } catch (IllegalArgumentException ex) {
        // Don't do anything, this just wasn't the right data type.
      }
    }
    if (storage == null) {
      throw new UnsupportedOperationException(
          "Cannot convert remixer item, maybe you forgot to add support for a new type?");
    }
    storage.key = item.getKey();
    storage.title = item.getTitle();
    return storage;
  }
}
