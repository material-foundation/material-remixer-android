package com.google.android.libraries.remixer.view;

import com.google.android.libraries.remixer.Remixer;

/**
 * An interface that all activities where there is a Remixer must implement.
 *
 * <p>This interface allows RemixerFragment to interact with the host activity and set
 * itself up with the Activity's {@link Remixer}.
 */
public interface RemixerActivity {
  Remixer getRemixer();
}
