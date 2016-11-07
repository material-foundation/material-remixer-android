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

import com.google.android.libraries.remixer.sync.SynchronizationMechanism;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Contains a list of {@link Variable}s or {@link Trigger}s.
 *
 * <p>The Remixer object is the heart and soul of Remixer as a framework, it coordinates syncing and
 * value changes.
 *
 * <p>For value and data syncing/persistence (both locally across activities, and persisting/saving
 * to cloud backends, etc) and keeping a global state, the Remixer object relies on a {@link
 * SynchronizationMechanism} set at initialization time (usually in your Application.onCreate()).
 *
 * <p>If you do not set a SynchronizationMechanism, remixer will not try to sync values across
 * different contexts (activities) or check for consistency among those. It is recommended you
 * always use one (even if it is just the LocalValueSyncing one) so you catch consistency errors.
 */
public class Remixer {

  private static Remixer instance;

  /**
   * This is a map of Remixer Item keys to a list of remixer items that have that key.
   *
   * <p>There may be several RemixerItems for the same key because the key can be reused in
   * different activities and the value has to be shared across those.
   */
  private HashMap<String, List<RemixerItem>> keyMap;

  /**
   * This is a map of contexts to a list of remixer items for the given context.
   */
  private HashMap<Object, List<RemixerItem>> contextMap;

  /**
   * The synchronization mechanism used to keep values in sync across different instances of the
   * variables and save/sync to other devices.
   */
  private SynchronizationMechanism synchronizationMechanism;

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

  /**
   * Visible only for testing. Users should only use {@link #getInstance()}.
   */
  public Remixer() {
    keyMap = new HashMap<>();
    contextMap = new HashMap<>();
  }

  /**
   * Set the synchronization mechanism to keep variables in sync locally and (possibly) with
   * external sources.
   *
   * <p>Remixer relies on a SynchronizationMechanism instance to be the source of truth of the
   * values and configuration, so the user should always set a SynchronizationMechanism.
   *
   * @throws IllegalStateException if a SynchronizationMechanism has been already set.
   */
  public void setSynchronizationMechanism(SynchronizationMechanism synchronizationMechanism) {
    if (this.synchronizationMechanism != null) {
      throw new IllegalStateException(
          "You can only set one synchronization mechanism in the app's lifetime");
    }
    this.synchronizationMechanism = synchronizationMechanism;
    synchronizationMechanism.setRemixerInstance(this);
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
    for (RemixerItem existingItem : listForKey) {
      if (remixerItem.getContext() != null
          && remixerItem.getContext() == existingItem.getContext()) {
        // An object with the same key for the same parent object, this shouldn't happen so throw
        // an exception.
        throw new DuplicateKeyException(
            String.format(
                Locale.getDefault(),
                "Duplicate key %s being used in class %s",
                remixerItem.getKey(),
                existingItem.getContext().getClass().getCanonicalName()
            ));
      }
    }
    if (synchronizationMechanism != null) {
      // Notify the synchronization mechanism, which will take care of keeping the values in sync
      // and checking compatibility.
      synchronizationMechanism.onAddingRemixerItem(remixerItem);
    }
    remixerItem.setRemixer(this);
    listForKey.add(remixerItem);
    getOrCreateItemList(remixerItem.getContext(), contextMap).add(remixerItem);
  }

  /**
   * Gets the list of items that have the given key.
   */
  public List<RemixerItem> getItemsWithKey(String key) {
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
      T key, HashMap<T, List<RemixerItem>> map) {
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
   * Notifies the synchronization mechanism that this variable's value has changed.
   */
  void onValueChanged(Variable variable) {
    synchronizationMechanism.onValueChanged(variable);
  }

  /**
   * Notifies the synchronization mechanism that a trigger was just triggered.
   */
  void onTrigger(Trigger trigger) {
    synchronizationMechanism.onTrigger(trigger);
  }

  /**
   * Handles the case in which an {@code activity} is destroyed by removing all its child remixes.
   */
  public void onActivityDestroyed(Object activity) {
    List<RemixerItem> list = getItemsWithContext(activity);
    if (list != null) {
      for (RemixerItem remixerItem : list) {
        getItemsWithKey(remixerItem.getKey()).remove(remixerItem);
      }
      contextMap.remove(activity);
    }
  }
}

