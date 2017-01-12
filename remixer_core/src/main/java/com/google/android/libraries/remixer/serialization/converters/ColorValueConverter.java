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
