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
 * A freeform string value that can be changed.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class StringRemix extends Remix<String> {

  /**
   * Creates a new StringRemix and runs the callback.
   *
   * @param title Displayable name for this Remix.
   * @param key The key used to store this Remix.
   * @param defaultValue The default value to use if none has been set.
   * @param callback Callback to run once the value is set. Can be null.
   * @param layoutId a layout id that renders this control on screen. Its root element
   *     must implement {@code com.google.android.libraries.remixer.view.RemixView<StringRemix>}.
   */
  public StringRemix(
      String title,
      String key,
      String defaultValue,
      RemixCallback<String> callback,
      int layoutId) {
    super(title, key, defaultValue, callback, layoutId);
  }

  @Override
  protected void checkValue(String value) {
    // Empty implementation, all values are accepted.
  }

  /**
   * Convenience builder for StringRemix.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the default value is not set, the empty string will be used as the default value.
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
    private String defaultValue = "";
    private RemixCallback<String> callback;
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

    public Builder setDefaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder setCallback(RemixCallback<String> callback) {
      this.callback = callback;
      return this;
    }

    public Builder setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new StringRemix created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key is missing
     */
    public StringRemix buildAndInit() {
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for StringRemix");
      }
      if (title == null) {
        title = key;
      }
      StringRemix remix = new StringRemix(title, key, defaultValue, callback, layoutId);
      remix.init();
      return remix;
    }
  }
}
