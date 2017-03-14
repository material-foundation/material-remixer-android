/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.serialization.converters;

import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for integer values.
 */
public class FloatValueConverter extends ValueConverter<Float, Float> {

  public FloatValueConverter(String dataType) {
    super(dataType);
  }

  @Override
  public Float parseValue(JsonElement element) {
    return element.getAsFloat();
  }

  @Override
  public JsonElement valueToJson(Float value) {
    return new JsonPrimitive(value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StoredVariable<Float> fromVariable(Variable<?> var) {
    StoredVariable<Float> storage = super.fromVariable(var);
    if (var instanceof RangeVariable) {
      RangeVariable range = (RangeVariable) var;
      storage.setMinValue(range.getMinValue());
      storage.setMaxValue(range.getMaxValue());
      storage.setIncrement(range.getIncrement());
    }
    return storage;
  }

  @Override
  public Float fromRuntimeType(Float value) {
    return value;
  }

  @Override
  public Float toRuntimeType(Float value) {
    return value;
  }
}
