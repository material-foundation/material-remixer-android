package com.google.android.libraries.remixer;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

/**
 * Base class for all Remix controls. This class implements calling the callback when necessary,
 * value checking, etc.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public abstract class Remix<T> {

  /**
   * The name to display in the UI for this remix.
   */
  private final String title;
  /**
   * The key to use to save to SharedPreferences. This needs to be unique across all remixes.
   */
  private final String key;
  /**
   * The callback to be executed when the value is updated.
   */
  private final RemixCallback callback;
  /**
   * The layout to inflate to display this remix
   */
  @LayoutRes
  private final int controlViewResourceId;
  /**
   * The currently selected value.
   */
  private T selectedValue;

  /**
   * Creates a new Remix.
   *
   * @param key The key to use to save to SharedPreferences. This needs to be unique across all
   * Remixes.
   * @param title The name to display in the UI
   * @param defaultValue The default value for this Remix.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param controlViewResourceId a layout to inflate when displaying this Remix in the UI. The root
   * view in this layout must implement RemixView.
   * @throws IllegalArgumentException defaultValue is invalid for this Remix. See {@link
   * #checkValue(Object)}.
   */
  // TODO(miguely): Add default value semantics to the defaultValue, currently it behaves mostly
  // as an initial value. It should be used in cases when the value is set to an invalid value from
  // SharedPreferences or Firebase.
  protected Remix(
      String title,
      String key,
      T defaultValue,
      @Nullable RemixCallback callback,
      @LayoutRes int controlViewResourceId) {
    this.key = key;
    this.title = title;
    // TODO(miguely): pull this out of SharedPreferences.
    this.selectedValue = defaultValue;
    this.callback = callback;
    this.controlViewResourceId = controlViewResourceId;
  }

  public String getTitle() {
    return title;
  }

  public String getKey() {
    return key;
  }

  public T getSelectedValue() {
    return selectedValue;
  }

  /**
   * Checks that the value passed in is a valid value, otherwise throws {@link
   * IllegalArgumentException}.
   *
   * @throws IllegalArgumentException An invalid value was passed in.
   */
  protected abstract void checkValue(T value);

  /**
   * Sets the selected value to a new value.
   *
   * <p>This needs to be implemented in each of the remixes that extend this class, it should throw
   * an IllegalArgumentException if the value is invalid.
   *
   * @param newValue Value to set.
   * @throws IllegalArgumentException newValue is an invalid value for this Remix.
   */
  public void setValue(T newValue) {
    checkValue(newValue);
    selectedValue = newValue;
    runCallback();
  }

  /**
   * Returns the layout id to inflate when displaying this Remix.
   *
   * <p>The root element of the provided layout must implement {@link
   * RemixView}.
   */
  @LayoutRes
  public int getControlViewResourceId() {
    return controlViewResourceId;
  }


  protected void runCallback() {
    if (callback != null) {
      callback.onValueSet(this);
    }
  }
}
