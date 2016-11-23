package com.google.android.libraries.remixer.serialization.converters;

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RangeVariable;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for color values.
 */
public class ColorValueConverter extends ValueConverter<Integer> {

  public ColorValueConverter(String dataType) {
    super(dataType);
  }

  private static int argb(int alpha, int red, int green, int blue) {
    return (alpha << 24) | (red << 16) | (green << 8) | blue;
  }

  private static int alpha(int color) {
    return color >>> 24;
  }

  private static int red(int color) {
    return (color >>> 16) & 0xFF;
  }

  private static int green(int color) {
    return (color >>> 8) & 0xFF;
  }

  private static int blue(int color) {
    return color & 0xFF;
  }

  @Override
  public Integer parseValue(JsonElement element) {
    JsonObject object = element.getAsJsonObject();
    return argb(
        object.get("a").getAsInt(),
        object.get("r").getAsInt(),
        object.get("g").getAsInt(),
        object.get("b").getAsInt());
  }

  @Override
  public JsonElement valueToJson(Integer value) {
    JsonObject object = new JsonObject();
    object.add("a", new JsonPrimitive(alpha(value)));
    object.add("r", new JsonPrimitive(red(value)));
    object.add("g", new JsonPrimitive(green(value)));
    object.add("b", new JsonPrimitive(blue(value)));
    return object;
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
        if (var instanceof ItemListVariable) {
          storage.setPossibleValues(((ItemListVariable<Integer>) var).getValueList());
        }
        return storage;
      }
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable<Color>");
  }
}
