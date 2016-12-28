package com.google.android.libraries.remixer.serialization.converters;

import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for boolean values.
 */
public class BooleanValueConverter extends ValueConverter<Boolean> {

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
  public StoredVariable<Boolean> fromVariable(Variable<?> var) {
    if (var.getDataType().getName().equals(dataType)) {
      StoredVariable<Boolean> storage = new StoredVariable<>();
      storage.setDataType(dataType);
      storage.setSelectedValue((Boolean) var.getSelectedValue());
      return storage;
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable<Boolean>");
  }
}
