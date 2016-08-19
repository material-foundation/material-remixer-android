package com.google.android.libraries.remixer.widget;

import android.support.annotation.NonNull;

import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.view.RemixerFragment;

/**
 * An interface that all remix widgets must implement.
 *
 * <p>This lets the {@link RemixerFragment} bind them to a {@link Remix} when they are inflated.
 */
public interface RemixWidget<T extends Remix<?>> {
  /**
   * Binds the remix to the widget in question.
   *
   * <p>Subclasses need to set up all the callbacks from the UI to set the appropriate value on the
   * backing Remix.
   */
  void bindRemix(@NonNull T remix);
}
