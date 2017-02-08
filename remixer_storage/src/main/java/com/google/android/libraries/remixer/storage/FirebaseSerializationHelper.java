/*
 * Copyright 2017 Google Inc.
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

package com.google.android.libraries.remixer.storage;

import static com.google.android.libraries.remixer.serialization.StoredVariable.CONSTRAINT_TYPE;
import static com.google.android.libraries.remixer.serialization.StoredVariable.DATA_TYPE;
import static com.google.android.libraries.remixer.serialization.StoredVariable.INCREMENT;
import static com.google.android.libraries.remixer.serialization.StoredVariable.KEY;
import static com.google.android.libraries.remixer.serialization.StoredVariable.LIMITED_TO_VALUES;
import static com.google.android.libraries.remixer.serialization.StoredVariable.MAX_VALUE;
import static com.google.android.libraries.remixer.serialization.StoredVariable.MIN_VALUE;
import static com.google.android.libraries.remixer.serialization.StoredVariable.SELECTED_VALUE;
import static com.google.android.libraries.remixer.serialization.StoredVariable.TITLE;

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class to deserialize Stored Variables. They cannot be directly deserialized because of
 * type erasure in Java.
 *
 * <p>Unless you know the type ahead of compile time, there is no way to pass a correctly formed
 * {@link GenericTypeIndicator} for firebase to use when deserializing StoredVariables or even Lists
 * for that matter. Since the whole point of remixer variables is to not know their type ahead of
 * time, these have to be manually deserialized.
 */
class FirebaseSerializationHelper {
  /**
   * Deserializes a value of type {@code clazz} from {@code dataSnapshot}'s child keyed by
   * {@code key}.
   */
  private static <T> T getValue(DataSnapshot dataSnapshot, String key, Class<T> clazz) {
    return dataSnapshot.child(key).getValue(clazz);
  }

  /**
   * Deserializes a list of values of type {@code clazz} from {@code dataSnapshot}'s child keyed by
   * {@code key}.
   */
  private static <T> List<T> getValueList(
      DataSnapshot dataSnapshot, String key, Class<T> serializableType) {
    List<T> list = new ArrayList<>();
    for (DataSnapshot child : dataSnapshot.child(key).getChildren()) {
      list.add(child.getValue(serializableType));
    }
    return list;
  }

  /**
   * Deserializes an entire {@link StoredVariable} from the given {@code dataSnapshot}.
   */
  @SuppressWarnings("unchecked")
  public static StoredVariable deserializeStoredVariable(DataSnapshot dataSnapshot) {
    StoredVariable variable = new StoredVariable();
    variable.setKey(getValue(dataSnapshot, KEY, String.class));
    variable.setTitle(getValue(dataSnapshot, TITLE, String.class));
    variable.setConstraintType(
        getValue(dataSnapshot, CONSTRAINT_TYPE, String.class));
    variable.setDataType(getValue(dataSnapshot, DATA_TYPE, String.class));
    DataType<?,?> dataType = Remixer.getDataType(variable.getDataType());
    variable.setSelectedValue(
        getValue(dataSnapshot, SELECTED_VALUE, dataType.getSerializableType()));
    if (dataSnapshot.hasChild(LIMITED_TO_VALUES)) {
      variable.setLimitedToValues(
          getValueList(dataSnapshot, LIMITED_TO_VALUES, dataType.getSerializableType()));

    }
    if (dataSnapshot.hasChild(MIN_VALUE)) {
      variable.setMinValue(
          getValue(dataSnapshot, MIN_VALUE, dataType.getSerializableType()));
    }
    if (dataSnapshot.hasChild(MAX_VALUE)) {
      variable.setMaxValue(
          getValue(dataSnapshot, MAX_VALUE, dataType.getSerializableType()));
    }
    if (dataSnapshot.hasChild(INCREMENT)) {
      variable.setIncrement(
          getValue(dataSnapshot, INCREMENT, dataType.getSerializableType()));
    }
    return variable;
  }
}
