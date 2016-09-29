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

import java.lang.ref.WeakReference;

/**
 * An item that can be displayed on Remixer's interfaces.
 *
 * <p>These are either {@link Variable}es or {@link Trigger}s.
 */
public abstract class RemixerItem {

  /**
   * The name to display in the UI for this remix.
   */
  private final String title;
  /**
   * The key to use to identify this item across storage and all the interfaces.
   */
  private final String key;
  /**
   * The layout to inflate to display this remixer item. If set to 0, the default layout associated
   * with the remixer item type will be used.
   */
  private final int layoutId;
  /**
   * A weak reference to the object that created this RemixerItem.
   *
   * <p>It should be a reference to an activity, but it isn't since remixer_core cannot depend on
   * Android classes. It is a weak reference in order not to leak the activity accidentally.
   */
  @SuppressWarnings("unchecked")
  private final WeakReference parentObject;
  /**
   * A copy of the parent object's class object. This will be necessary to know whether an object
   * is of the same class as the parent object, even after the parent object has been reclaimed by
   * the Garbage Collector.
   */
  @SuppressWarnings("unchecked")
  private final Class parentObjectClass;

  /**
   * Constructs a new RemixerItem with the given key, title and layoutId.
   */
  @SuppressWarnings("unchecked")
  protected RemixerItem(String title, String key, Object parentObject, int layoutId) {
    this.title = title;
    this.key = key;
    this.parentObject = new WeakReference(parentObject);
    this.parentObjectClass = parentObject.getClass();
    this.layoutId = layoutId;
  }

  /**
   * Checks whether the parent object is the same as the parameter.
   */
  public boolean isParentObject(Object object) {
    Object localParentObject = parentObject.get();
    if (localParentObject == null) {
      return false;
    }
    return localParentObject.equals(parentObject);
  }

  /**
   * Checks whether the parameter is of the same class as the (possibly already reclaimed) parent
   * object.
   */
  public boolean isSameClassAsParentObject(Object object) {
    return parentObjectClass.equals(object.getClass());
  }

  public String getTitle() {
    return title;
  }

  public String getKey() {
    return key;
  }

  /**
   * Returns the layout id to inflate when displaying this Remixer item.
   */
  public int getLayoutId() {
    return layoutId;
  }

  abstract void clearCallback();
}
