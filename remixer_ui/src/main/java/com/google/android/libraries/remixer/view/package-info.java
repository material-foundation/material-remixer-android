/**
 * Contains the code to display the
 * {@link com.google.android.libraries.remixer.view.RemixerFragment}.
 *
 * <p>You should only need to instantiate it and push it to the FragmentManager from an activity
 * that implements {@link com.google.android.libraries.remixer.view.RemixerActivity}:
 *
 * <pre><code>
 * import com.google.android.libraries.remixer.view.RemixerActivity;
 * import com.google.android.libraries.remixer.view.RemixerFragment;
 * import com.google.android.remixer.Remixer;
 *
 * class MyActivity implements RemixerActivity {
 *   Remixer remixer;
 *   RemixerFragment remixerFragment;
 *
 *   &#064;Override
 *   public Remixer getRemixer() {
 *     return remixer;
 *   }
 *
 *   // ...
 *
 *   void showRemixer() {
 *     if (remixerFragment == null) {
 *       remixerFragment = RemixerFragment.newInstance();
 *     }
 *     remixerFragment.show(getSupportFragmentManager(), "FragmentKey");
 *   }
 * }
 *
 * </code></pre>
 */
package com.google.android.libraries.remixer.view;