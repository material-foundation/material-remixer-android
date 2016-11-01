/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.storage;

import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.storage.json.SerializableRemixerContents;
import com.google.android.libraries.remixer.storage.json.StoredVariable;
import com.google.android.libraries.remixer.storage.json.SupportedDataType;
import com.google.android.libraries.remixer.sync.SynchronizationMechanism;

/**
 * A purely-local implementation of a Synchronization Mechanism. This handles keeping values in sync
 * locally.
 */
public class LocalValueSyncing implements SynchronizationMechanism {

  SerializableRemixerContents serializableRemixerContents = new SerializableRemixerContents();
  Remixer remixer;

  @Override
  public void setRemixerInstance(Remixer remixer) {
    this.remixer = remixer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onAddingRemixerItem(RemixerItem item) {
    serializableRemixerContents.addItem(item);
    StoredVariable storedVariable = serializableRemixerContents.getItem(item.getKey());
    if (!storedVariable.getDataType().equals(SupportedDataType.TRIGGER)) {
      // Check the value for updates.
      Variable variable = (Variable) item;
      variable.setValueWithoutNotifyingOthers(storedVariable.getSelectedValue());
    }
  }

  @Override
  public void onValueChanged(Variable variable) {
    serializableRemixerContents.setValue(variable);
  }

  @Override
  public void onTrigger(Trigger trigger) {
    // Triggers don' have values so
  }
}
