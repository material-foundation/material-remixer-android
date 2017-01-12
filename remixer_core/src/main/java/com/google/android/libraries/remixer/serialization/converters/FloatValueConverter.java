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
public class FloatValueConverter extends ValueConverter<Float> {

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
    if (var.getDataType().getName().equals(dataType)) {
      StoredVariable<Float> storage = new StoredVariable<>();
      storage.setDataType(dataType);
      storage.setSelectedValue((Float) var.getSelectedValue());
      if (var instanceof RangeVariable) {
        RangeVariable range = (RangeVariable) var;
        storage.setMinValue(range.getMinValue());
        storage.setMaxValue(range.getMaxValue());
        storage.setIncrement(range.getIncrement());
      } else if (var instanceof ItemListVariable) {
        storage.setPossibleValues(((ItemListVariable<Float>) var).getLimitedToValues());
      }
      return storage;
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable<Float>");
  }
}
