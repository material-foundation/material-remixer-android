package com.google.android.libraries.remixer;

/**
 * This object is a callback for when the value is set.
 */
public interface RemixCallback<T> {
  /**
   * This method will be called when the value is set, even during the initial set up.
   *
   * @param remix The Remix that was set, if you need its current value you can call
   * remix.getSelectedValue().
   */
  void onValueSet(Remix<T> remix);
}
