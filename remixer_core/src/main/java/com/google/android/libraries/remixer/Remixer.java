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

  private HashMap<String, List<RemixerItem>> keyMap;
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
  public void addItem(RemixerItem remixerItem) {
    List<RemixerItem> listForKey = getItemsWithKey(remixerItem.getKey());
    List<RemixerItem> itemsToRemove = new ArrayList<>();
    for (RemixerItem existingItem : listForKey) {
      existingItem.isCompatibleWith(remixerItem);
      if (!existingItem.hasParentObject()) {
        // The parent activity has been reclaimed by the OS already. It has no callback and it's
        // still around for keeping the value in sync, saving and checking consistency across types.
        if (existingItem.isSameClassAsParentObject(remixerItem.getParentObject())) {
          // At this point, we can remove the existing item, since a full replacement is coming
          // in with the new item: same key coming from the same parent class. The only reason why
          // this was being kept to this point was to make sure no remixer item came with
          // incompatible types from a different activity and keeping the value for storage, the
          // same can be achieved by the new one.
          itemsToRemove.add(existingItem);
        }
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
    listForKey.add(remixerItem);
    remixerItems.add(remixerItem);
  }

  private List<RemixerItem> getItemsWithKey(String key) {
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
   * Gets all the Remixer Items associated with the parent object {@code parent}.
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
}
