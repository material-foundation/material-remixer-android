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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 * Gson serializer and dezerialzer for StoredVariable.
 *
 * {@link StoredVariable} acts as an intermediate format for serialization between a regular Remixer
 * Item and Json. Having this serializer and deserializer in the middle allows the same class to
 * work for serializing several data types, having a converter for special cases (like Color).
 */
class StoredVariableSerializer
    implements JsonSerializer<StoredVariable>, JsonDeserializer<StoredVariable> {

  @Override
  public StoredVariable deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject object = json.getAsJsonObject();
    JsonPrimitive jsonDataType = object.getAsJsonPrimitive(StoredVariable.DATA_TYPE);
    String dataType = jsonDataType.getAsString();
    StoredVariable variable = null;
    for (DataType type : Remixer.getInstance().getRegisteredDataTypes()) {
      if (dataType.equals(type.getName())) {
        variable = type.getConverter().deserialize(json);
        break;
      }
    }
    if (variable == null) {
      // Unknown data type! Throw an exception!
      throw new JsonParseException("Unknown data type for variable, cannot parse.");
    }
    return variable;
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonElement serialize(
      StoredVariable src, Type typeOfSrc, JsonSerializationContext context) {
    ValueConverter converter = null;
    for (DataType type : Remixer.getInstance().getRegisteredDataTypes()) {
      if (src.dataType.equals(type.getName())) {
        converter = type.getConverter();
        break;
      }
    }
    if (converter == null) {
      throw new UnsupportedOperationException("unknown data type " + src.dataType);
    }
    return converter.serialize(src);
  }
}
