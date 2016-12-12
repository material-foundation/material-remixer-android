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

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.Remixer;
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
public class StoredVariable<T> {

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
   * DataType}.
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

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public T getSelectedValue() {
    return selectedValue;
  }

  public void setSelectedValue(T selectedValue) {
    this.selectedValue = selectedValue;
  }

  public List<T> getPossibleValues() {
    return possibleValues;
  }

  public void setPossibleValues(List<T> possibleValues) {
    this.possibleValues = possibleValues;
  }

  public T getMinValue() {
    return minValue;
  }

  public void setMinValue(T minValue) {
    this.minValue = minValue;
  }

  public T getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(T maxValue) {
    this.maxValue = maxValue;
  }

  public T getIncrement() {
    return increment;
  }

  public void setIncrement(T increment) {
    this.increment = increment;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    StoredVariable<?> variable = (StoredVariable<?>) obj;
    if (!isCompatibleWith(variable)) {
      return false;
    }

    if (title != null ? !title.equals(variable.title) : variable.title != null) {
      return false;
    }
    return selectedValue != null
        ? selectedValue.equals(variable.selectedValue) : variable.selectedValue == null;
  }

  /**
   * Checks whether the configuration of this stored variable matches the configuration of the
   * argument. The configuration explicitly excludes the value for comparison, the value may be
   * synced later if the configurations are compatible
   */
  @SuppressWarnings("PMD.CollapsibleIfStatements")
  public boolean isCompatibleWith(StoredVariable<?> variable) {

    if (!key.equals(variable.key)) {
      return false;
    }
    if (!dataType.equals(variable.dataType)) {
      return false;
    }
    if (possibleValues != null
        ? !possibleValues.equals(variable.possibleValues) : variable.possibleValues != null) {
      return false;
    }
    if (minValue != null ? !minValue.equals(variable.minValue) : variable.minValue != null) {
      return false;
    }
    if (maxValue != null ? !maxValue.equals(variable.maxValue) : variable.maxValue != null) {
      return false;
    }
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
    StoredVariable storedVariable = null;
    for (DataType type : Remixer.getRegisteredDataType()) {
      try {
        storedVariable = type.getConverter().fromRemixerItem(item);
        break;
      } catch (IllegalArgumentException ex) {
        // Don't do anything, this just wasn't the right data type.
        storedVariable = null;
      }
    }
    if (storedVariable == null) {
      throw new UnsupportedOperationException(
          "Cannot convert remixer item, maybe you forgot to add support for a new type?");
    }
    storedVariable.key = item.getKey();
    storedVariable.title = item.getTitle();
    return storedVariable;
  }
}
