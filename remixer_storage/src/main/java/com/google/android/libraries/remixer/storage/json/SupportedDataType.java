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

import android.graphics.Color;
import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;
import com.google.android.remixer.examples.remixerstorage.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Enum that contains all the supported data types for Remixer serialization.
 *
 * Each data dataTypeSerializableString has a dataTypeSerializableString identifier string and a {@link ValueConverter} which does most of the
 * heavywork for serialization.
 */
enum SupportedDataType {

  BOOLEAN("boolean", new ValueConverter<Boolean>("boolean") {
    @Override
    public Boolean parseValue(JsonElement element) {
      return element.getAsBoolean();
    }

    @Override
    JsonElement valueToJson(Boolean value) {
      return new JsonPrimitive(value);
    }

    @Override
    StoredVariable<Boolean> fromRemixerItem(RemixerItem item) {
      if (item instanceof Variable) {
        Variable var = (Variable) item;
        if (var.getDataType().getName().equals(DataType.BOOLEAN.getName())) {
          StoredVariable<Boolean> storage = new StoredVariable<>();
          storage.dataType = BOOLEAN.getDataTypeSerializableString();
          storage.selectedValue = (Boolean) var.getSelectedValue();
          return storage;
        }
      }
      throw new IllegalArgumentException(
          "Passed an incompatible object to convert to StoredVariable<Boolean>");
    }
  }),
  COLOR("color", new ValueConverter<Integer>("color") {
    @Override
    public Integer parseValue(JsonElement element) {
      JsonObject object = element.getAsJsonObject();
      return Color.argb(
          object.get("a").getAsInt(),
          object.get("r").getAsInt(),
          object.get("g").getAsInt(),
          object.get("b").getAsInt());
    }

    @Override
    JsonElement valueToJson(Integer value) {
      JsonObject object = new JsonObject();
      object.add("a", new JsonPrimitive(Color.alpha(value)));
      object.add("r", new JsonPrimitive(Color.red(value)));
      object.add("g", new JsonPrimitive(Color.green(value)));
      object.add("b", new JsonPrimitive(Color.blue(value)));
      return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<Integer> fromRemixerItem(RemixerItem item) {
      if (item instanceof Variable) {
        Variable var = (Variable) item;
        if (var.getDataType().getName().equals(DataType.COLOR.getName())) {
          StoredVariable<Integer> storage = new StoredVariable<>();
          storage.dataType = COLOR.getDataTypeSerializableString();
          storage.selectedValue = (Integer) var.getSelectedValue();
          if (var instanceof ItemListVariable) {
            storage.possibleValues = ((ItemListVariable<Integer>) var).getValueList();
          }
          return storage;
        }
      }
      throw new IllegalArgumentException(
          "Passed an incompatible object to convert to StoredVariable<Color>");
    }
  }),
  NUMBER("number", new ValueConverter<Integer>("number") {
    @Override
    public Integer parseValue(JsonElement element) {
      // TODO(miguely): This needs to move to float once we actually support floats. I expect this
      // will cause exceptions once Cloud syncing is done, so we need to fix this before then.
      return element.getAsInt();
    }

    @Override
    JsonElement valueToJson(Integer value) {
      return new JsonPrimitive(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<Integer> fromRemixerItem(RemixerItem item) {
      if (item instanceof Variable) {
        Variable var = (Variable) item;
        if (var.getDataType().getName().equals(DataType.NUMBER.getName())) {
          StoredVariable<Integer> storage = new StoredVariable<>();
          storage.dataType = NUMBER.getDataTypeSerializableString();
          storage.selectedValue = (Integer) var.getSelectedValue();
          if (var instanceof RangeVariable) {
            RangeVariable range = (RangeVariable) var;
            storage.minValue = range.getMinValue();
            storage.maxValue = range.getMaxValue();
            storage.increment = range.getIncrement();
          }
          if (var instanceof ItemListVariable) {
            storage.possibleValues = ((ItemListVariable<Integer>) var).getValueList();
          }
          return storage;
        }
      }
      throw new IllegalArgumentException(
          "Passed an incompatible object to convert to StoredVariable<Integer>");
    }
  }),
  STRING("string", new ValueConverter<String>("string") {
    @Override
    public String parseValue(JsonElement element) {
      return element.getAsString();
    }

    @Override
    JsonElement valueToJson(String value) {
      return new JsonPrimitive(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<String> fromRemixerItem(RemixerItem item) {
      if (item instanceof Variable) {
        Variable var = (Variable) item;
        if (var.getDataType().getName().equals(DataType.STRING.getName())) {
          StoredVariable<String> storage = new StoredVariable<>();
          storage.dataType = STRING.getDataTypeSerializableString();
          storage.selectedValue = (String) var.getSelectedValue();
          if (var instanceof ItemListVariable) {
            storage.possibleValues = ((ItemListVariable<String>) var).getValueList();
          }
          return storage;
        }
      }
      throw new IllegalArgumentException(
          "Passed an incompatible object to convert to StoredVariable<String>");
    }
  }),
  TRIGGER("trigger", new ValueConverter<Void>("trigger") {
    @Override
    Void parseValue(JsonElement element) {
      return null;
    }

    @Override
    JsonElement valueToJson(Void value) {
      return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<Void> fromRemixerItem(RemixerItem item) {
      if (item instanceof Trigger) {
        StoredVariable<Void> storage = new StoredVariable<>();
        storage.dataType = TRIGGER.getDataTypeSerializableString();
        return storage;
      }
      throw new IllegalArgumentException(
          "Passed an incompatible object to convert to StoredVariable<Void> (Trigger)");
    }
  });

  private String dataTypeSerializableString;

  private ValueConverter valueConverter;

  SupportedDataType(String dataTypeSerializableString, ValueConverter valueConverter) {
    this.dataTypeSerializableString = dataTypeSerializableString;
    this.valueConverter = valueConverter;
  }

  /**
   * Gets the string that represents this dataType when serialized. When deserializing it's used to
   * identify the parsing logic to use.
   */
  String getDataTypeSerializableString() {
    return dataTypeSerializableString;
  }

  /**
   * Gets the value converter that contains all the export and parsing logic used in the
   * serialization and deserialization processes.
   */
  ValueConverter getValueConverter() {
    return valueConverter;
  }

}
