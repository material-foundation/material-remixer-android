package com.google.android.libraries.remixer;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

/**
 * A Remix for toggling a boolean value.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class BooleanRemix extends Remix<Boolean> {


  /**
   * Creates a new Boolean Remix and runs its callback.
   *
   * @param title The name to display in the UI
   * @param key The key to use to save to SharedPreferences. This needs to be unique across all
   * Remixes.
   * @param defaultValue The default value for this Remix.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param controlViewResourceId a layout to inflate when displaying this Remix in the UI. The root
   * view in this layout must implement RemixView.
   * @throws IllegalArgumentException defaultValue is invalid for this Remix. See {@link
   * #checkValue(Object)}.
   */
  public BooleanRemix(
      String title, String key, Boolean defaultValue, @Nullable RemixCallback callback,
      @LayoutRes int controlViewResourceId) {
    super(title, key, defaultValue, callback, controlViewResourceId);
    runCallback();
  }



  @Override
  protected void checkValue(Boolean value) {
    // There are only two possible values, it doesn't make sense to check them. :)
    return;
  }
}
