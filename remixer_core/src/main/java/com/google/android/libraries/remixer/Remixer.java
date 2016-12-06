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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Contains a list of {@link Variable}es.
 */
public class Remixer {

  private static Remixer instance;

  /**
   * This is a map of Remixer Item keys to a list of remixer items that have that key.
   *
   * <p>There may be several RemixerItems for the same key because the key can be reused in
   * different activities and the value has to be shared across those.
   */
  private Map<String, List<RemixerItem>> keyMap;

  /**
   * This is a map of contexts to a list of remixer items for the given context.
   */
  private Map<Object, List<RemixerItem>> contextMap;

  /**
   * Datatypes keyed by their serializable name.
   */
  private Map<String, DataType> registeredDataTypes;
  
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
    contextMap = new HashMap<>();
    registeredDataTypes = new HashMap<>();
  }

  /**
   * This adds a remixer item ({@link Variable} or {@link Trigger}) to be tracked and displayed.
   * Checks that the remixer item is compatible with the existing remixer items with the same key.
   *
   * <p>This method also removes old remixer items whose contexts have been reclaimed by the garbage
   * collector which are being replaced by items from the same class of context. No items are
   * removed until equivalent ones from the same context class are added to replace them. This
   * guarantees that no incompatible items for the same key are ever accepted.
   *
   * @param remixerItem The remixer item to be added. It must have a context object otherwise it
   *     will never be displayed, and thus not be editable.
   * @throws IncompatibleRemixerItemsWithSameKeyException Other items with the same key have been
   *     added other contexts with incompatible types.
   * @throws DuplicateKeyException Another item with the same key was added for the same context.
   */
  @SuppressWarnings("unchecked")
  public void addItem(RemixerItem remixerItem) {
    List<RemixerItem> listForKey = getOrCreateItemList(remixerItem.getKey(), keyMap);
    List<RemixerItem> itemsToRemove = new ArrayList<>();
    for (RemixerItem existingItem : listForKey) {
      existingItem.assertIsCompatibleWith(remixerItem);
      if (!existingItem.hasContext()) {
        // The context activity has been reclaimed by the OS already. It has no callback and it's
        // still around for keeping the value in sync, saving and checking consistency across types.
        // Since we're adding a new item that has been asserted to be compatible, it is not
        // necessary to keep this instance around.
        itemsToRemove.add(existingItem);
      } else {
        // The context activity is still alive and kicking.
        if (existingItem.matchesContext(remixerItem.getContext())) {
          // An object with the same key for the same context, this shouldn't happen so throw
          // an exception.
          throw new DuplicateKeyException(
              String.format(
                  Locale.getDefault(),
                  "Duplicate key %s being used in class %s",
                  remixerItem.getKey(),
                  remixerItem.getContext().getClass().getCanonicalName()
              ));
        }
      }
    }
    if (remixerItem instanceof Variable && listForKey.size() > 0) {
      // Make sure that variables use their current value if it has been modified in another
      // context.
      // If any modification has been made in any other context to the value of variables with the
      // same key, otherVariable will have the newest value.
      Variable otherVariable = (Variable) listForKey.get(0);
      // At this point newVariable will have the default value only.
      Variable newVariable = (Variable) remixerItem;
      // Make newVariable have the current value found in variables that have already been added to
      // the Remixer.
      newVariable.setValueWithoutNotifyingOthers(otherVariable.getSelectedValue());
    }
    for (RemixerItem remove : itemsToRemove) {
      listForKey.remove(remove);
      // no need to remove from contextMap, contextMap has already removed the whole list of items
      // with that context that was reclaimed, see cleanUpCallbacks().
    }
    listForKey.add(remixerItem);
    remixerItem.setRemixer(this);
    getOrCreateItemList(remixerItem.getContext(), contextMap).add(remixerItem);
  }

  /**
   * Gets the list of items that have the given key.
   */
  List<RemixerItem> getItemsWithKey(String key) {
    return keyMap.get(key);
  }

  /**
   * Gets all the Remixer Items associated with {@code context}. {@code context} is expected to be
   * an Activity, it is Object here because remixer_core cannot depend on the Android SDK.
   */
  public List<RemixerItem> getItemsWithContext(Object context) {
    return contextMap.get(context);
  }



  /**
   * Gets a list of RemixerItems for the given {@code key} from the {@code map} passed in, if such a
   * mapping does not exist, it adds a mapping to a new empty list.
   */
  private static <T> List<RemixerItem> getOrCreateItemList(
      T key, Map<T, List<RemixerItem>> map) {
    List<RemixerItem> list = null;
    if (map.containsKey(key)) {
      list = map.get(key);
    } else {
      list = new ArrayList<>();
      map.put(key, list);
    }
    return list;
  }

  /**
   * Register a new data type that can be used with Remixer.
   */
  public void registerDataType(DataType dataType) {
    if (registeredDataTypes.containsKey(dataType.getName())) {
      throw new IllegalStateException("Adding a data type that has already been added, name: "
          + dataType.getName() );
    }
    registeredDataTypes.put(dataType.getName(), dataType);
  }

  public DataType getDataType(String name) {
    return registeredDataTypes.get(name);
  }

  public Collection<DataType> getRegisteredDataType() {
    return registeredDataTypes.values();
  }

  /**
   * Removes callbacks for all remixes whose context is {@code activity}. This makes sure {@code
   * activity} doesn't leak through its callbacks.
   */
  public void cleanUpCallbacks(Object activity) {
    if (contextMap.containsKey(activity)) {
      for (RemixerItem remixerItem : contextMap.get(activity)) {
        remixerItem.clearCallback();
        remixerItem.clearContext();
      }
      contextMap.remove(activity);
    }
  }
}

