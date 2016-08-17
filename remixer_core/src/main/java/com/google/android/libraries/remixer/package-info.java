/**
 * The main remixer package contains all of the logic for Remixes and a main point of entry class,
 * {@link com.google.android.libraries.remixer.Remixer}, which contains all of the active Remixes.
 *
 * <p>Most of the logic is implemented by the base class
 * {@link com.google.android.libraries.remixer.Remix}. Subclasses are tasked with checking whether
 * values are valid or not and providing a LayoutRes to display it on the Remixer UI.
 *
 * <b>None of the Remix classes are thread-safe and should only be used from the UI Thread. This way
 * the callbacks are guaranteed to be run on the UI thread.</b>
 */
package com.google.android.libraries.remixer;