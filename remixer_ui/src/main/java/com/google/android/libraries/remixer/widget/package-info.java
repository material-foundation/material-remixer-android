/**
 * Contains all of the Remixer UI Elements that represent an individual Remix in the UI, these are
 * the RemixWidget family of classes.
 *
 * <p>Each RemixWidget is bound to a corresponding layout id (i.e.
 * {@link com.google.android.libraries.remixer.widget.BooleanRemixWidget} is instantiated by inflating
 * {@code R.layout.boolean_remix_widget}).
 *
 * <p>All of the Remix UI elements must implement
 * {@link com.google.android.libraries.remixer.widget.RemixWidget}, that allows the RemixWidget to have its
 * corresponding {@link com.google.android.libraries.remixer.Remix} assigned.
 */
package com.google.android.libraries.remixer.widget;