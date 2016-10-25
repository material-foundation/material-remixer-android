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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Contains a list of {@link Variable}es.
 */
public class Remixer {

  private static Remixer instance;

  /**
   * This is a map of Remixer Item keys to a list of remixer items that have that key.
   *
   * <p>There may be several RemixerItems for the same key because the key can be reused in
   * different activities and the value has to be sared across those.
   */
  private HashMap<String, List<RemixerItem>> keyMap;
  /**
   * A list of all the Remixer Items added to the Remixer.
   */
  private List<RemixerItem> remixerItems;

  /**
   * Gets the singleton for Remixer.
   *
   * <p><b>Note this operation is not thread safe and should only be called from the main
   * thread.</b>
   */
  public static Remixer getInstance() {
    if (instance == null) {
      instance = new Remixer();
    }
    return instance;
  }

  Remixer() {
    keyMap = new HashMap<>();
    remixerItems = new ArrayList<>();
  }

  /**
   * This adds a remixer item ({@link Variable} or {@link Trigger}) to be tracked and displayed.
   * Checks that the remixer item is compatible with the existing remixer items with the same key.
   *
   * <p>This method also removes old remixer items whose parent objects have been reclaimed by the
   * Garbage collector which are being replaced by items from the same class of parent objects.
   * No items are removed until equivalent ones from the same parent object class are added to
   * replace them. This guarantees that no incompatible items for the same key are ever accepted.
   *
   * @param remixerItem The remixer item to be added.
   * @throws IncompatibleRemixerItemsWithSameKeyException Other items with the same key have been
   *     added by other parent objects with incompatible types.
   * @throws DuplicateKeyException Another item with the same key was added by the same parent
   *     object.
   */
  @SuppressWarnings("unchecked")
  public void addItem(RemixerItem remixerItem) {
    List<RemixerItem> listForKey = getItemsWithKey(remixerItem.getKey());
    List<RemixerItem> itemsToRemove = new ArrayList<>();
    for (RemixerItem existingItem : listForKey) {
      existingItem.assertIsCompatibleWith(remixerItem);
      if (!existingItem.hasParentObject()) {
        // The parent activity has been reclaimed by the OS already. It has no callback and it's
        // still around for keeping the value in sync, saving and checking consistency across types.
        // Since we're adding a new item that has been asserted to be compatible, it is not
        // necessary to keep this instance around.
        itemsToRemove.add(existingItem);
      } else {
        // The parent activity is still alive and kicking.
        if (existingItem.isParentObject(remixerItem.getParentObject())) {
          // An object with the same key for the same parent object, this shouldn't happen so throw
          // an exception.
          throw new DuplicateKeyException(
              String.format(
                  Locale.getDefault(),
                  "Duplicate key %s being used in class %s",
                  remixerItem.getKey(),
                  remixerItem.getParentObject().getClass().getCanonicalName()
              ));
        }
      }
    }
    for (RemixerItem remove : itemsToRemove) {
      listForKey.remove(remove);
      remixerItems.remove(remove);
    }
    if (remixerItem instanceof Variable && listForKey.size() > 0) {
      // Make sure that variables use their current value if it has been modified in another
      // context.
      Variable otherVariable = (Variable) listForKey.get(0);
      Variable newVariable = (Variable) remixerItem;
      newVariable.setValueWithoutNotifyingOthers(otherVariable.getSelectedValue());
    }
    listForKey.add(remixerItem);
    remixerItem.setRemixer(this);
    remixerItems.add(remixerItem);
  }

  List<RemixerItem> getItemsWithKey(String key) {
    List<RemixerItem> list = null;
    if (keyMap.containsKey(key)) {
      list = keyMap.get(key);
    } else {
      list = new ArrayList<>();
      keyMap.put(key, list);
    }
    return list;
  }

  public List<RemixerItem> getRemixerItems() {
    return remixerItems;
  }

  /**
   * Gets all the Remixer Items associated with the parent object {@code parent}. {@code parent} is
   * expected to be an Activity, it is Object here because remixer_core cannot depend on the Android
   * SDK.
   */
  public List<RemixerItem> getRemixerItemsForParentObject(Object parent) {
    List<RemixerItem> result = new ArrayList<>();
    for (RemixerItem item : remixerItems) {
      if (item.isParentObject(parent)) {
        result.add(item);
      }
    }
    return result;
  }

  /**
   * Removes callbacks for all remixes whose parent object is {@code activity}. This makes sure
   * {@code activity} doesn't leak through its callbacks.
   */
  public void cleanUpCallbacks(Object activity) {
    for (RemixerItem remixerItem : remixerItems) {
      if (remixerItem.isParentObject(activity)) {
        remixerItem.clearCallback();
      }
    }
  }
}
