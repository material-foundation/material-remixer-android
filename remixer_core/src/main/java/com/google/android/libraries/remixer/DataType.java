package com.google.android.libraries.remixer;

import com.google.android.libraries.remixer.serialization.ValueConverter;
import com.google.android.libraries.remixer.serialization.converters.BooleanValueConverter;
import com.google.android.libraries.remixer.serialization.converters.ColorValueConverter;
import com.google.android.libraries.remixer.serialization.converters.IntegerValueConverter;
import com.google.android.libraries.remixer.serialization.converters.StringValueConverter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
  private final Class<T> valueClass;

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
  private final Map<Class<? extends Variable>, Integer> layoutIdForVariableType =
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
    if (!name.equals(converter.getDataType())) {
      throw new AssertionError(String.format(
          Locale.getDefault(),
          "The data type %s has a converter whose data type doesn't match, %s",
          name, converter.getDataType()));
    }
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

  public void setLayoutIdForVariableType(Class<? extends Variable> clazz, int layoutId) {
    layoutIdForVariableType.put(clazz, layoutId);
  }

  public int getLayoutIdForVariableType(Class<? extends Variable> clazz) {
    return layoutIdForVariableType.get(clazz);
  }

  public String getName() {
    return name;
  }

  public Class<T> getValueClass() {
    return valueClass;
  }

  public ValueConverter<T> getConverter() {
    return converter;
  }

  // ======= Default data types defined here.

  private static final String KEY_BOOLEAN = "__DataTypeBoolean__";
  private static final String KEY_COLOR = "__DataTypeColor__";
  private static final String KEY_NUMBER = "__DataTypeNumber__";
  private static final String KEY_STRING = "__DataTypeString__";

  public static final DataType<Boolean> BOOLEAN = new DataType<>(
      KEY_BOOLEAN, Boolean.class, new BooleanValueConverter(KEY_BOOLEAN));

  public static final DataType<Integer> COLOR = new DataType<>(
      KEY_COLOR, Integer.class, new ColorValueConverter(KEY_COLOR));

  public static final DataType<Integer> NUMBER = new DataType<>(
      KEY_NUMBER, Integer.class, new IntegerValueConverter(KEY_NUMBER));

  public static final DataType<String> STRING = new DataType<>(
      KEY_STRING, String.class, new StringValueConverter(KEY_STRING));
}
