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

/**
 * A Remix for toggling a boolean value.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class BooleanRemix extends Remix<Boolean> {

  /**
   * Creates a new Boolean Remix and runs its callback.
   *
   * @param title The name to display in the UI.
   * @param key The key to use to save to SharedPreferences. This needs to be unique across all
   *     Remixes.
   * @param defaultValue The default value for this Remix.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param layoutId A layout to inflate when displaying this Remix in the UI.
   * @throws IllegalArgumentException {@code defaultValue} is invalid for this Remix. See {@link
   *     #checkValue(Object)}.
   */
  public BooleanRemix(
      String title,
      String key,
      Boolean defaultValue,
      RemixCallback callback,
      int layoutId) {
    super(title, key, defaultValue, callback, layoutId);
    runCallback();
  }

  @Override
  protected void checkValue(Boolean value) {
    // There are only two possible values, it doesn't make sense to check them. :)
  }

  /**
   * Convenience builder for BooleanRemix.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the default value is not set, false will be used as the default value.
   * <li>If the layout id is not set, the default layout will be used.
   * <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key is mandatory. If it's missing, an {@link IllegalArgumentException}
   * will be thrown.
   */
  public static class Builder {

    private String key;
    private String title;
    private boolean defaultValue = false;
    private RemixCallback<Boolean> callback;
    private int layoutId = 0;

    public Builder() {}

    public Builder setKey(String key) {
      this.key = key;
      return this;
    }

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setDefaultValue(boolean defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder setCallback(RemixCallback<Boolean> callback) {
      this.callback = callback;
      return this;
    }

    public Builder setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new BooleanRemix created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key is missing
     */
    public BooleanRemix build() {
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for BooleanRemix");
      }
      if (title == null) {
        title = key;
      }
      return new BooleanRemix(title, key, defaultValue, callback, layoutId);
    }
  }
}
