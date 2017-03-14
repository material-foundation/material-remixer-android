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
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for string values.
 */
public class StringValueConverter extends ValueConverter<String, String> {

  public StringValueConverter(String dataType) {
    super(dataType);
  }

  @Override
  public String parseValue(JsonElement element) {
    return element.getAsString();
  }

  @Override
  public JsonElement valueToJson(String value) {
    return new JsonPrimitive(value);
  }

  @Override
  public String fromRuntimeType(String value) {
    return value;
  }

  @Override
  public String toRuntimeType(String value) {
    return value;
  }
}
