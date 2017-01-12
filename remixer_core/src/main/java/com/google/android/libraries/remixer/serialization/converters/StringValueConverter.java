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
