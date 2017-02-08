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
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.Variable;

import java.util.List;

/**
 * This is an intermediate format that conforms to the agreed upon cross-platform storage and sync
 * protocol.
 *
 * <p>This is never used for anything other than storage and syncing and is meant to be converted to
 * a regular {@link com.google.android.libraries.remixer.Variable} as soon as it's completely
 * parsed.
 */
public class StoredVariable<T> {

  /**
   * The serializable string to represent the constraintType on values for variables of the
   * {@link Variable} class.
   */
  public final static String VARIABLE_CONSTRAINT = "__ConstraintTypeNone__";

  /**
   * The serializable string to represent the constraintType on values for variables of the
   * {@link ItemListVariable} class.
   */
  public final static String ITEM_LIST_VARIABLE_CONSTRAINT = "__ConstraintTypeList__";

  /**
   * The serializable string to represent the constraintType on values for variables of the
   * {@link RangeVariable} class.
   */
  public final static String RANGE_VARIABLE_CONSTRAINT = "__ConstraintTypeRange__";

  // Json dictionary keys to serialize this object
  public static final String KEY = "key";
  public static final String TITLE = "title";
  public static final String CONSTRAINT_TYPE = "constraintType";
  public static final String DATA_TYPE = "dataType";
  public static final String SELECTED_VALUE = "selectedValue";
  public static final String LIMITED_TO_VALUES = "limitedToValues";
  public static final String MIN_VALUE = "minValue";
  public static final String MAX_VALUE = "maxValue";
  public static final String INCREMENT = "increment";

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
  /**
   * The constraintType on this variable.
   *
   * <p>If this is a regular {@link Variable} it's "none", if it's an {@link ItemListVariable} it's
   * "list", and if it is a {@link RangeVariable} it's "range".
   */
  String constraintType;

  /**
   * The currently selected value for the variable.
   */
  T selectedValue;
  /**
   * The list of possible values, if this is not null then this StoredVariable represents a {@link
   * com.google.android.libraries.remixer.ItemListVariable}.
   */
  List<T> limitedToValues;

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

  public String getConstraintType() {
    return constraintType;
  }

  public void setConstraintType(String constraintType) {
    this.constraintType = constraintType;
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

  public List<T> getLimitedToValues() {
    return limitedToValues;
  }

  public void setLimitedToValues(List<T> limitedToValues) {
    this.limitedToValues = limitedToValues;
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
    if (limitedToValues != null
        ? !limitedToValues.equals(variable.limitedToValues) : variable.limitedToValues != null) {
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
    result = 31 * result + (limitedToValues != null ? limitedToValues.hashCode() : 0);
    result = 31 * result + (minValue != null ? minValue.hashCode() : 0);
    result = 31 * result + (maxValue != null ? maxValue.hashCode() : 0);
    result = 31 * result + (increment != null ? increment.hashCode() : 0);
    return result;
  }

  /**
   * Creates a Stored variable from a existing RemixerItem.
   */
  public static StoredVariable fromVariable(Variable item) {
    StoredVariable storedVariable = null;
    for (DataType type : Remixer.getRegisteredDataTypes()) {
      try {
        storedVariable = type.getConverter().fromVariable(item);
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
    storedVariable.constraintType = item.getSerializableConstraints();
    return storedVariable;
  }
}
