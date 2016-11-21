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
import java.util.Locale;

/**
 * An item that can be displayed on Remixer's interfaces.
 *
 * <p>These are either {@link Variable}es or {@link Trigger}s.
 */
public abstract class RemixerItem {

  /**
   * The name to display in the UI for this remixer item.
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
   * A weak reference to this RemixerItem's context. The RemixerItem's lifecycle is tied to its
   * contexts'.
   *
   * <p>It should be a reference to an activity, but it isn't since remixer_core cannot depend on
   * Android classes. It is a weak reference in order not to leak the activity accidentally.
   */
  private final WeakReference<Object> context;
  /**
   * The remixer instance this RemixerItem has been attached to.
   */
  protected Remixer remixer;
  /**
   * The data type held in this RemixerItem.
   */
  private DataType dataType;

  /**
   * Constructs a new RemixerItem with the given key, title and layoutId.
   */
  protected RemixerItem(String title, String key, Object context, int layoutId, DataType dataType) {
    this.title = title;
    this.key = key;
    this.context = new WeakReference<Object>(context);
    this.layoutId = layoutId;
    this.dataType = dataType;
  }

  public DataType getDataType() {
    return dataType;
  }

  /**
   * Checks whether the context is the same as the parameter.
   */
  public boolean matchesContext(Object object) {
    if (object == null) {
      return false;
    }
    return context.get() == object;
  }

  /**
   * Checks whether the context has been reclaimed.
   */
  public boolean hasContext() {
    return context.get() != null;
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

  /**
   * Removes the callback for this remixer item, it is used to avoid leaks through callbacks once
   * activities are destroyed.
   */
  abstract void clearCallback();

  /**
   * Checks whether {@code item} is compatible with this RemixerItem.
   * @throws IncompatibleRemixerItemsWithSameKeyException if {@code item} has the same key as this
   *     object, and they are of different types or otherwise incompatible.
   */
  final void assertIsCompatibleWith(RemixerItem item) {
    if (item.getKey().equals(key) && !dataType.equals(item.dataType)) {
      throw new IncompatibleRemixerItemsWithSameKeyException(String.format(
          Locale.getDefault(),
          "Trying to add two remixer items with key %s and incompatible types %s and %s",
          key, dataType.getName(), item.getDataType().getName()));

    }
  }

  /**
   * Returns the context.
   */
  Object getContext() {
    return context.get();
  }

  /**
   * Clears the context reference to simulate reclaiming the context in tests.
   *
   * <p><b>Visible only for testing.</b>
   */
  void clearContext() {
    context.clear();
  }

  /**
   * Set the current remixer instance. This allows the Remixer item to notify other items with the
   * same key.
   */
  public void setRemixer(Remixer remixer) {
    this.remixer = remixer;
  }

  public abstract static class Builder<T extends RemixerItem, C> {

    protected String key;
    protected String title;
    protected Object context;
    protected int layoutId = 0;
    protected C callback;
    protected DataType dataType;

    public Builder<T, C> setKey(String key) {
      this.key = key;
      return this;
    }

    public Builder<T, C>  setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder<T, C>  setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    public Builder<T, C>  setContext(Object context) {
      this.context = context;
      return this;
    }

    public Builder<T, C>  setCallback(C callback) {
      this.callback = callback;
      return this;
    }

    public Builder<T, C>  setDataType(DataType dataType) {
      this.dataType = dataType;
      return this;
    }

    protected void checkBaseFields() {
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for RemixerItem");
      }
      if (context == null) {
        throw new IllegalArgumentException("context cannot be unset for RemixerItem");
      }
      if (dataType == null) {
        throw new IllegalArgumentException("dataType cannot be unset for RemixerItem");
      }
      if (title == null) {
        title = key;
      }
    }

    /**
     * Returns the built RemixerItem. Implementors must call {@link #checkBaseFields()}.
     * @throws IllegalArgumentException if the minimally required fields were not set or their
     *     configuration is invalid.
     */
    public abstract T build();
  }

}
