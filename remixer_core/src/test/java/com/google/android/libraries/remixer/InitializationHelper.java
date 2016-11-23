/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer;

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.Remixer;
import java.util.HashSet;

/**
 * Helper class to properly initialize remixer instances.
 */
public class InitializationHelper {

  public static void init() {
    Remixer.clearRegisteredDataTypes();
    Remixer.registerDataType(DataType.BOOLEAN);
    Remixer.registerDataType(DataType.COLOR);
    Remixer.registerDataType(DataType.NUMBER);
    Remixer.registerDataType(DataType.STRING);
    Remixer.registerDataType(DataType.TRIGGER);
  }
}
