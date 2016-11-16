package com.google.android.libraries.remixer;

import java.util.HashMap;

/**
 * The data type for each RemixerItem. The data type is used to determine default layoutIDs and to
 * help serialization.
 */
public class DataType {

  /**
   * The serializable, unique name for this data type.
   */
  private final String name;

  /**
   * The class of the values contained by this variable.
   */
  private final Class valueClass;

  /**
   * Map of default layout ids for this datatype when used with a specific RemixerItem class.
   *
   * <p>The key for this map is the specific RemixerItem subclass, and the value is the default
   * layout to use when a RemixerItem of the specific subclass has this data type.
   */
  private final HashMap<Class<? extends RemixerItem>, Integer> layoutIdForRemixerItemType =
      new HashMap<>();

  public DataType(String name, Class valueClass) {
    this.name = name;
    this.valueClass = valueClass;
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
  // ======= Default data types defined here.

  public static final DataType BOOLEAN = new DataType("boolean", Boolean.class);

  public static final DataType COLOR = new DataType("color", Integer.class);

  public static final DataType NUMBER = new DataType("number", Integer.class);

  public static final DataType STRING = new DataType("string", String.class);

  public static final DataType TRIGGER = new DataType("trigger", Void.class);
}
