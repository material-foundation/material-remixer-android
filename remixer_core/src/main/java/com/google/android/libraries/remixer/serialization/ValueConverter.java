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

import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Variable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;

/**
 * Helper object that abstracts the type-dependent parts of parsing a Variable from Json and
 * converting it from a java object into its JSON representation.
 * @param <T> the data type stored in the variable.
 */
public abstract class ValueConverter<T> {

  /**
   * The data type this converter is used for.
   */
  protected String dataType;

  public ValueConverter(String dataType) {
    this.dataType = dataType;
  }

  /**
   * Gets the data type name for this converter.
   */
  public String getDataType() {
    return dataType;
  }

  /**
   * Returns an object of type T that holds the value in the current Json Element.
   */
  public abstract T parseValue(JsonElement element);

  /**
   * Returns a JsonElement that represents the value passed in.
   */
  public abstract JsonElement valueToJson(T value);

  /**
   * Creates a StoredVariable that represents the data in {@code variable} if {@code item} is of
   * this type.
   * @throws IllegalArgumentException if {@code item} does not match this type.
   */
  public abstract StoredVariable<T> fromVariable(Variable<?> variable);

  /**
   * Deserializes a JsonElement that contains a StoredVariable.
   */
  public StoredVariable<T> deserialize(JsonElement json) {
    StoredVariable<T> result = new StoredVariable<>();
    JsonObject object = json.getAsJsonObject();
    result.selectedValue = parseValue(object.get(StoredVariable.SELECTED_VALUE));
    result.constraints = object.get(StoredVariable.CONSTRAINTS).getAsString();

    if (ItemListVariable.SERIALIZABLE_CONSTRAINTS.equals(result.constraints)) {
      deserializePossibleValues(result, object.get(StoredVariable.POSSIBLE_VALUES));
    } else if (RangeVariable.SERIALIZABLE_CONSTRAINTS.equals(result.constraints)) {
      deserializeRangeProperties(result, object);
    }
    result.dataType = dataType;
    result.key = object.getAsJsonPrimitive(StoredVariable.KEY).getAsString();
    result.title = object.getAsJsonPrimitive(StoredVariable.TITLE).getAsString();
    return result;
  }

  private void deserializeRangeProperties(StoredVariable<T> result, JsonObject object) {
    result.minValue = parseValue(object.getAsJsonPrimitive(StoredVariable.MIN_VALUE));
    result.maxValue = parseValue(object.getAsJsonPrimitive(StoredVariable.MAX_VALUE));
    result.increment = parseValue(object.getAsJsonPrimitive(StoredVariable.INCREMENT));
  }

  private void deserializePossibleValues(StoredVariable<T> result, JsonElement possibleValuesElement) {
    if (possibleValuesElement != null) {
      JsonArray array = possibleValuesElement.getAsJsonArray();
      result.possibleValues = new ArrayList<>();
      for (JsonElement arrayElement : array) {
        result.possibleValues.add(parseValue(arrayElement));
      }
    }
  }

  /**
   * Serializes a StoredVariable into a JsonElement.
   */
  public JsonElement serialize(StoredVariable<T> src) {
    JsonObject object = new JsonObject();
    object.add(StoredVariable.KEY, new JsonPrimitive(src.key));
    object.add(StoredVariable.TITLE, new JsonPrimitive(src.title));
    object.add(StoredVariable.DATA_TYPE, new JsonPrimitive(src.dataType));
    object.add(StoredVariable.SELECTED_VALUE, valueToJson(src.selectedValue));
    object.add(StoredVariable.CONSTRAINTS, new JsonPrimitive(src.constraints));
    if (ItemListVariable.SERIALIZABLE_CONSTRAINTS.equals(src.constraints)) {
      JsonArray possibleValues = new JsonArray();
      for (T item : src.possibleValues) {
        possibleValues.add(valueToJson(item));
      }
      object.add(StoredVariable.POSSIBLE_VALUES, possibleValues);
    }
    if (RangeVariable.SERIALIZABLE_CONSTRAINTS.equals(src.constraints)) {
      object.add(StoredVariable.MIN_VALUE, valueToJson(src.minValue));
      object.add(StoredVariable.MAX_VALUE, valueToJson(src.maxValue));
      object.add(StoredVariable.INCREMENT, valueToJson(src.increment));
    }
    return object;
  }
}
