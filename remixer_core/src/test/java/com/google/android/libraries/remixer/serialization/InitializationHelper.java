package com.google.android.libraries.remixer.serialization;

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.Remixer;
import java.util.HashSet;

/**
 * Helper class to properly initialize remixer instances.
 */
class InitializationHelper {

  private static HashSet<Remixer> initializedRemixerInstances = new HashSet<>();

  static void init(Remixer instance) {
    if (!initializedRemixerInstances.contains(instance)) {
      instance.registerDataType(DataType.BOOLEAN);
      instance.registerDataType(DataType.COLOR);
      instance.registerDataType(DataType.NUMBER);
      instance.registerDataType(DataType.STRING);
      initializedRemixerInstances.add(instance);
    }
  }
}
