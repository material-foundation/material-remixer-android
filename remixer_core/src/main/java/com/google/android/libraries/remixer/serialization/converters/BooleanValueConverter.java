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
