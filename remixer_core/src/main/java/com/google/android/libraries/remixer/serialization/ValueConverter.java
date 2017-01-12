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
 * @param <RuntimeType> the data type that the remixer core framework uses.
 * @param <SerializableType> the data type to use during serialization.
 */
public abstract class ValueConverter<RuntimeType, SerializableType> {

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
   * Returns an object of type SerializableType that holds the value in the current Json Element.
   */
  public abstract SerializableType parseValue(JsonElement element);

  /**
   * Returns a JsonElement that represents the value passed in.
   */
  public abstract JsonElement valueToJson(SerializableType value);

  /**
   * Converts values from the runtime type to the serializable type.
   */
  public abstract SerializableType fromRuntimeType(RuntimeType value);

  /**
   * Converts values from the serializable type to the runtime type.
   */
  public abstract RuntimeType toRuntimeType(SerializableType value);

  /**
   * Deserializes a JsonElement that contains a StoredVariable.
   */
  public StoredVariable<SerializableType> deserialize(JsonElement json) {
    StoredVariable<SerializableType> result = new StoredVariable<>();
    JsonObject object = json.getAsJsonObject();
    result.selectedValue = parseValue(object.get(StoredVariable.SELECTED_VALUE));
    result.constraintType = object.get(StoredVariable.CONSTRAINT_TYPE).getAsString();

    if (StoredVariable.ITEM_LIST_VARIABLE_CONSTRAINT.equals(result.constraintType)) {
      deserializeLimitedToValues(result, object.get(StoredVariable.LIMITED_TO_VALUES));
    } else if (StoredVariable.RANGE_VARIABLE_CONSTRAINT.equals(result.constraintType)) {
      deserializeRangeProperties(result, object);
    }
    result.dataType = dataType;
    result.key = object.getAsJsonPrimitive(StoredVariable.KEY).getAsString();
    result.title = object.getAsJsonPrimitive(StoredVariable.TITLE).getAsString();
    return result;
  }

  private void deserializeRangeProperties(StoredVariable<SerializableType> result, JsonObject object) {
    result.minValue = parseValue(object.getAsJsonPrimitive(StoredVariable.MIN_VALUE));
    result.maxValue = parseValue(object.getAsJsonPrimitive(StoredVariable.MAX_VALUE));
    result.increment = parseValue(object.getAsJsonPrimitive(StoredVariable.INCREMENT));
  }

  private void deserializeLimitedToValues(
      StoredVariable<SerializableType> result, JsonElement limitedToValuesElement) {
    if (limitedToValuesElement != null) {
      JsonArray array = limitedToValuesElement.getAsJsonArray();
      result.limitedToValues = new ArrayList<>();
      for (JsonElement arrayElement : array) {
        result.limitedToValues.add(parseValue(arrayElement));
      }
    }
  }

  /**
   * Serializes a StoredVariable into a JsonElement.
   */
  public JsonElement serialize(StoredVariable<SerializableType> src) {
    JsonObject object = new JsonObject();
    object.add(StoredVariable.KEY, new JsonPrimitive(src.key));
    object.add(StoredVariable.TITLE, new JsonPrimitive(src.title));
    object.add(StoredVariable.DATA_TYPE, new JsonPrimitive(src.dataType));
    object.add(StoredVariable.SELECTED_VALUE, valueToJson(src.selectedValue));
    object.add(StoredVariable.CONSTRAINT_TYPE, new JsonPrimitive(src.constraintType));
    if (StoredVariable.ITEM_LIST_VARIABLE_CONSTRAINT.equals(src.constraintType)) {
      JsonArray limitedToValues = new JsonArray();
      for (SerializableType item : src.limitedToValues) {
        limitedToValues.add(valueToJson(item));
      }
      object.add(StoredVariable.LIMITED_TO_VALUES, limitedToValues);
    }
    if (StoredVariable.RANGE_VARIABLE_CONSTRAINT.equals(src.constraintType)) {
      object.add(StoredVariable.MIN_VALUE, valueToJson(src.minValue));
      object.add(StoredVariable.MAX_VALUE, valueToJson(src.maxValue));
      object.add(StoredVariable.INCREMENT, valueToJson(src.increment));
    }
    return object;
  }

  /**
   * Creates a StoredVariable that represents the data in {@code variable} if {@code item} is of
   * this type.
   * @throws IllegalArgumentException if {@code item} does not match this type.
   */
  @SuppressWarnings("unchecked")
  public StoredVariable<SerializableType> fromVariable(Variable<?> var) {
    if (var.getDataType().getName().equals(dataType)) {
      StoredVariable<SerializableType> storage = new StoredVariable<>();
      storage.setDataType(dataType);
      storage.setSelectedValue(fromRuntimeType((RuntimeType) var.getSelectedValue()));
      if (var instanceof ItemListVariable) {
        ArrayList<SerializableType> possibleValues = new ArrayList<>();
        for (RuntimeType value : ((ItemListVariable<RuntimeType>) var).getLimitedToValues()) {
          possibleValues.add(fromRuntimeType(value));
        }
        storage.setLimitedToValues(possibleValues);
      }
      return storage;
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable for type " + dataType);
  }
}
