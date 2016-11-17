package com.google.android.libraries.remixer;

import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.android.libraries.remixer.serialization.converters.BooleanValueConverter;
import com.google.android.libraries.remixer.serialization.converters.ColorValueConverter;
import com.google.android.libraries.remixer.serialization.converters.IntegerValueConverter;
import com.google.android.libraries.remixer.serialization.converters.StringValueConverter;
import com.google.android.libraries.remixer.serialization.converters.TriggerValueConverter;
import java.util.HashMap;

/**
 * The data type for each RemixerItem. The data type is used to determine default layoutIDs and to
 * help serialization.
 */
public class DataType<T> {

  /**
   * The serializable, unique name for this data type.
   */
  private final String name;

  /**
   * The class of the values contained by this variable.
   */
  private final Class valueClass;

  /**
   * The value converter that aids in the serialization process.
   */
  private final ValueConverter<T> converter;
  /**
   * Map of default layout ids for this datatype when used with a specific RemixerItem class.
   *
   * <p>The key for this map is the specific RemixerItem subclass, and the value is the default
   * layout to use when a RemixerItem of the specific subclass has this data type.
   */
  private final HashMap<Class<? extends RemixerItem>, Integer> layoutIdForRemixerItemType =
      new HashMap<>();

  /**
   * Constructs a datatype with the given {@code name}, that takes values of type {@code valueClass}
   * and uses {@code converter} to serialize.
   *
   * <p>Note {@code converter} has a {@link ValueConverter#dataType} field that must be initialized
   * to the same as {@code name}.
   */
  public DataType(String name, Class<T> valueClass, ValueConverter<T> converter) {
    this.name = name;
    this.valueClass = valueClass;
    this.converter = converter;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    DataType dataType = (DataType) obj;
    if (!name.equals(dataType.name)) {
      return false;
    }
    return valueClass.equals(dataType.valueClass);

  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + valueClass.hashCode();
    return result;
  }

  public void setLayoutIdForRemixerItemType(Class<? extends RemixerItem> clazz, int layoutId) {
    layoutIdForRemixerItemType.put(clazz, layoutId);
  }

  public int getLayoutIdForRemixerItemType(Class<? extends RemixerItem> clazz) {
    return layoutIdForRemixerItemType.get(clazz);
  }

  public String getName() {
    return name;
  }

  public Class getValueClass() {
    return valueClass;
  }

  public ValueConverter<T> getConverter() {
    return converter;
  }

  // ======= Default data types defined here.

  public static final DataType<Boolean> BOOLEAN = new DataType<>(
      "boolean", Boolean.class, new BooleanValueConverter("boolean"));

  public static final DataType<Integer> COLOR = new DataType<>(
      "color", Integer.class, new ColorValueConverter("color"));

  public static final DataType<Integer> NUMBER = new DataType<>(
      "number", Integer.class, new IntegerValueConverter("number"));

  public static final DataType<String> STRING = new DataType<>(
      "string", String.class, new StringValueConverter("string"));

  public static final DataType<Void> TRIGGER = new DataType<>(
      "trigger", Void.class, new TriggerValueConverter("trigger"));
}
