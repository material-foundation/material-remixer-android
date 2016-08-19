package com.google.android.libraries.remixer.view;

import com.google.android.libraries.remixer.Remixer;

/**
 * Interface that all activities that use Remixer must implement.
 *
 * <p>This interface allows {@link RemixerFragment} to interact with the host activity and set
 * itself up with the Activity's {@link Remixer}.
 */
public interface RemixerActivity {
  Remixer getRemixer();
}
