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
 * Each data type has a type identifier string and a {@link ValueConverter} which does most of the
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
        if (var.getVariableType() == Boolean.class) {
          StoredVariable<Boolean> storage = new StoredVariable<>();
          storage.dataType = BOOLEAN.getType();
          storage.selectedValue = (Boolean) var.getSelectedValue();
          return storage;
        }
      }
      return null;
    }

    @Override
    public StoredVariable<Boolean> createStoredVariable() {
      return new StoredVariable<>();
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
        if (var.getVariableType() == Integer.class
            && var.getLayoutId() == R.layout.color_list_variable_widget) {
          StoredVariable<Integer> storage = new StoredVariable<>();
          storage.dataType = COLOR.getType();
          storage.selectedValue = (Integer) var.getSelectedValue();
          if (var instanceof ItemListVariable) {
            storage.possibleValues = ((ItemListVariable<Integer>) var).getValueList();
          }
          return storage;
        }
      }
      return null;
    }

    @Override
    public StoredVariable<Integer> createStoredVariable() {
      return new StoredVariable<>();
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
    public StoredVariable<Integer> createStoredVariable() {
      return new StoredVariable<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<Integer> fromRemixerItem(RemixerItem item) {
      if (item instanceof Variable) {
        Variable var = (Variable) item;
        if (var.getVariableType() == Integer.class
            && var.getLayoutId() != R.layout.color_list_variable_widget) {
          StoredVariable<Integer> storage = new StoredVariable<>();
          storage.dataType = NUMBER.getType();
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
      return null;
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
    public StoredVariable<String> createStoredVariable() {
      return new StoredVariable<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<String> fromRemixerItem(RemixerItem item) {
      if (item instanceof Variable) {
        Variable var = (Variable) item;
        if (var.getVariableType() == String.class) {
          StoredVariable<String> storage = new StoredVariable<>();
          storage.dataType = STRING.getType();
          storage.selectedValue = (String) var.getSelectedValue();
          if (var instanceof ItemListVariable) {
            storage.possibleValues = ((ItemListVariable<String>) var).getValueList();
          }
          return storage;
        }
      }
      return null;
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
    StoredVariable<Void> createStoredVariable() {
      return new StoredVariable<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    StoredVariable<Void> fromRemixerItem(RemixerItem item) {
      if (item instanceof Trigger) {
        StoredVariable<Void> storage = new StoredVariable<>();
        storage.dataType = TRIGGER.getType();
        return storage;
      }
      return null;
    }
  });

  SupportedDataType(String type, ValueConverter valueConverter) {
    this.type = type;
    this.valueConverter = valueConverter;
  }
  private String type;

  private ValueConverter valueConverter;

  public String getType() {
    return type;
  }

  public ValueConverter getValueConverter() {
    return valueConverter;
  }

}
