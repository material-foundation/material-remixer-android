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

import com.google.android.libraries.remixer.serialization.GsonProvider;
import com.google.android.libraries.remixer.serialization.SerializedColor;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;

/**
 * A value converter for color values.
 */
public class ColorValueConverter extends ValueConverter<Integer, SerializedColor> {

  public ColorValueConverter(String dataType) {
    super(dataType);
  }

  @Override
  public SerializedColor parseValue(JsonElement element) {
    return GsonProvider.getInstance().fromJson(element, SerializedColor.class);
  }

  @Override
  public JsonElement valueToJson(SerializedColor value) {
    return GsonProvider.getInstance().toJsonTree(value);
  }

  @Override
  public SerializedColor fromRuntimeType(Integer value) {
    return new SerializedColor(value.intValue());
  }

  @Override
  public Integer toRuntimeType(SerializedColor value) {
    return value.toAndroidColor();
  }
}
