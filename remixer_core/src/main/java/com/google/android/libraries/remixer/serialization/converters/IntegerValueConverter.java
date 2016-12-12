package com.google.android.libraries.remixer.serialization.converters;

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for integer values.
 */
public class IntegerValueConverter extends ValueConverter<Integer> {

  public IntegerValueConverter(String dataType) {
    super(dataType);
  }

  @Override
  public Integer parseValue(JsonElement element) {
    return element.getAsInt();
  }

  @Override
  public JsonElement valueToJson(Integer value) {
    return new JsonPrimitive(value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public StoredVariable<Integer> fromRemixerItem(RemixerItem item) {
    if (item instanceof Variable) {
      Variable var = (Variable) item;
      if (var.getDataType().getName().equals(dataType)) {
        StoredVariable<Integer> storage = new StoredVariable<>();
        storage.setDataType(dataType);
        storage.setSelectedValue((Integer) var.getSelectedValue());
        if (var instanceof RangeVariable) {
          RangeVariable range = (RangeVariable) var;
          storage.setMinValue(range.getMinValue());
          storage.setMaxValue(range.getMaxValue());
          storage.setIncrement(range.getIncrement());
        } else if (var instanceof ItemListVariable) {
          storage.setPossibleValues(((ItemListVariable<Integer>) var).getValueList());
        }
        return storage;
      }
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable<Integer>");
  }
}
