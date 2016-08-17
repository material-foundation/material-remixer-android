package com.google.android.libraries.remixer.widget;

import android.support.annotation.NonNull;

import com.google.android.libraries.remixer.Remix;

/**
 * All remix views implement this interface that lets them set the remix in question.
 */
public interface RemixWidget<T extends Remix<?>> {
  /**
   * Binds the remix to the view.
   *
   * <p>Subclasses need to set up all the callbacks from the UI to set the appropriate value on the
   * backing Remix.
   */
  void bindRemix(@NonNull T remix);
}
