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

package com.google.android.libraries.remixer.storage;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.GsonProvider;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import com.google.android.libraries.remixer.sync.LocalValueSyncing;
import com.google.gson.Gson;

/**
 * A {@link com.google.android.libraries.remixer.sync.SynchronizationMechanism} that stores values
 * in a SharedPreferences object.
 */
public class LocalStorage extends LocalValueSyncing {
  private final SharedPreferences preferences;
  private final Gson gson;

  public LocalStorage(Context context, String preferenceFileName) {
    preferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
    gson = GsonProvider.getInstance();
    for (Object data : preferences.getAll().values()) {
      // Assume all objects are actually JSON strings.
      StoredVariable<?> variable = gson.fromJson(data.toString(), StoredVariable.class);
      serializableRemixerContents.addItem(variable);
    }
  }

  private void writeRemixerItem(final String key) {
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(key, gson.toJson(serializableRemixerContents.getItem(key)));
    editor.apply();
  }

  @Override
  public void onAddingRemixerItem(RemixerItem item) {
    super.onAddingRemixerItem(item);
    writeRemixerItem(item.getKey());
  }

  @Override
  public void onValueChanged(Variable variable) {
    super.onValueChanged(variable);
    // TODO: Consider delaying these writes (all of them) to accomodate for fast changes.
    // This may not be necessary since using SharedPreferences.Editor#apply() already delays writes.
    writeRemixerItem(variable.getKey());
  }
}
