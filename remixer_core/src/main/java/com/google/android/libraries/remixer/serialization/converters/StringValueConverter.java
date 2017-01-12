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
public class StringValueConverter extends ValueConverter<String> {

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
  @SuppressWarnings("unchecked")
  public StoredVariable<String> fromVariable(Variable<?> var) {
    if (var.getDataType().getName().equals(dataType)) {
      StoredVariable<String> storage = new StoredVariable<>();
      storage.setDataType(dataType);
      storage.setSelectedValue((String) var.getSelectedValue());
      if (var instanceof ItemListVariable) {
        storage.setLimitedToValues(((ItemListVariable<String>) var).getLimitedToValues());
      }
      return storage;
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable<String>");
  }
}
