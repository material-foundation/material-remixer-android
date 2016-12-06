package com.google.android.libraries.remixer.serialization.converters;

import com.google.android.libraries.remixer.ItemListVariable;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A value converter for triggers. Values are nonexistent but it is still necessary for
 * serialization.
 */
public class TriggerValueConverter extends ValueConverter<Void> {

  public TriggerValueConverter(String dataType) {
    super(dataType);
  }

  @Override
  public Void parseValue(JsonElement element) {
    return null;
  }

  @Override
  public JsonElement valueToJson(Void value) {
    return null;
  }

  @Override
  public StoredVariable<Void> fromRemixerItem(RemixerItem item) {
    if (item instanceof Trigger) {
      Trigger trigger = (Trigger) item;
      StoredVariable<Void> storage = new StoredVariable<>();
      storage.setDataType(dataType);
      return storage;
    }
    throw new IllegalArgumentException(
        "Passed an incompatible object to convert to StoredVariable<String>");
  }
}
