package com.google.android.libraries.remixer;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

/**
 * A freeform string value that can be changed.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class StringRemix extends Remix<String> {

  /**
   * Creates a new StringRemix and runs the callback.
   *
   * @param title Displayable name for this Remix
   * @param key The key used to store this Remix
   * @param defaultValue The default value to use if none has been set.
   * @param callback Callback to run once the value is set. Can be null.
   * @param controlViewResourceId a layout id that renders this control on screen. Its root element
   * must implement {@code com.google.android.libraries.remixer.view.RemixView<StringRemix>}
   * @throws IllegalArgumentException if a list of valid values has been passed and the defaultValue
   * is not in it.
   */
  public StringRemix(
      String title,
      String key,
      @Nullable String defaultValue,
      @Nullable RemixCallback<String> callback,
      @LayoutRes int controlViewResourceId) {
    super(title, key, defaultValue, callback, controlViewResourceId);
    runCallback();
  }

  @Override
  protected void checkValue(String value) {
    // Empty implementation, all values are accepted.
  }
}
