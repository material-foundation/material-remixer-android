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

import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for boolean values.
 */
public class BooleanValueConverter extends ValueConverter<Boolean, Boolean> {

  public BooleanValueConverter(String dataType) {
    super(dataType);
  }

  @Override
  public Boolean parseValue(JsonElement element) {
    return element.getAsBoolean();
  }

  @Override
  public JsonElement valueToJson(Boolean value) {
    return new JsonPrimitive(value);
  }

  @Override
  public Boolean fromRuntimeType(Boolean value) {
    return value;
  }

  @Override
  public Boolean toRuntimeType(Boolean value) {
    return value;
  }
}
