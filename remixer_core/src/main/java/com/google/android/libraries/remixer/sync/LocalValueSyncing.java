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

package com.google.android.libraries.remixer.sync;

import com.google.android.libraries.remixer.DataType;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.RemixerItem;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.Variable;
import com.google.android.libraries.remixer.serialization.SerializableRemixerContents;
import com.google.android.libraries.remixer.serialization.StoredVariable;
import java.util.List;

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
    if (!storedVariable.getDataType().equals(DataType.TRIGGER)) {
      // Check the value for updates.
      Variable variable = (Variable) item;
      variable.setValueWithoutNotifyingOthers(storedVariable.getSelectedValue());
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onValueChanged(Variable variable) {
    serializableRemixerContents.setValue(variable);
    List<RemixerItem> itemList = remixer.getItemsWithKey(variable.getKey());
    for (RemixerItem item : itemList) {
      if (item != variable) {
        ((Variable) item).setValueWithoutNotifyingOthers(variable.getSelectedValue());
      }
    }
  }

  @Override
  public void onTrigger(Trigger trigger) {
    List<RemixerItem> itemList = remixer.getItemsWithKey(trigger.getKey());
    for (RemixerItem item : itemList) {
      if (item != trigger) {
        ((Trigger) item).triggerWithoutTriggeringOthers();
      }
    }
  }
}
