/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.ui.gesture;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;

/**
 * A Gesture Listener that expects multi-finger swipes in a direction to trigger the display of a
 * RemixerFragment.
 *
 * <p>It is configurable, allowing you to set the number of fingers (two or more), and the direction
 * of the swipe. It can be set up by calling {@link #attach(FragmentActivity, Direction, int,
 * RemixerFragment)} or {@link RemixerFragment#attachToGesture(FragmentActivity, Direction, int)}.
 */
public class GestureListener implements View.OnTouchListener {

  /**
   * Minimum movement in pixels to consider this a successful swipe
   */
  private static final int THRESHOLD = 100;
  /**
   * The FragmentManager for the activity, it is used to show the RemixerFragment.
   */
  private final FragmentManager fragmentManager;
  /**
   * Minimum number of fingers to swipe to trigger the callback.
   */
  private final int numberOfFingers;
  /**
   * Direction to swipe.
   */
  private final Direction direction;
  /**
   * The remixer fragment to show once the swipe succeeds.
   */
  private final RemixerFragment remixerFragment;

  /**
   * Map of all the fingers that are currently touching the screen.
   */
  private SparseArray<FingerPosition> fingerPositions = new SparseArray<>();

  /**
   * Attaches a GestureListener to {@code activity} that watches for swipes in the direction {@code
   * direction} with at least {@code numberOfFingers} and shows {@code remixerFragment} when found.
   */
  public static void attach(
      FragmentActivity activity,
      Direction direction,
      int numberOfFingers,
      RemixerFragment remixerFragment) {
    activity.findViewById(android.R.id.content).setOnTouchListener(
        new GestureListener(
            direction, numberOfFingers, activity.getSupportFragmentManager(), remixerFragment));
  }

  private GestureListener(
      Direction direction,
      int numberOfFingers,
      FragmentManager fragmentManager,
      RemixerFragment remixerFragment) {
    if (numberOfFingers < 2) {
      throw new IllegalArgumentException("GestureListener requires at least 2 fingers");
    }
    this.direction = direction;
    this.numberOfFingers = numberOfFingers;
    this.fragmentManager = fragmentManager;
    this.remixerFragment = remixerFragment;
  }

  @Override
  @SuppressLint("ClickableViewAccessibility")
  public boolean onTouch(View view, MotionEvent motionEvent) {
    int pointerIndex = motionEvent.getActionIndex();
    int pointerId = motionEvent.getPointerId(pointerIndex);
    FingerPosition position;
    switch (motionEvent.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        // Paranoia mode, if it's the first finger down make sure that we're in a clean slate
        fingerPositions.clear();
        // fall-through
      case MotionEvent.ACTION_POINTER_DOWN:
        position =
            new FingerPosition(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));
        fingerPositions.put(pointerId, position);
        break;
      case MotionEvent.ACTION_MOVE:
        // When the action is MotionEvent.ACTION_MOVE, the action index is meaningless, it is
        // always the original pointer from ACTION_DOWN, you have to iterate through all of them
        // because they all could contain updates.
        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
          int movedPointerId = motionEvent.getPointerId(i);
          position = fingerPositions.get(movedPointerId);
          position.setCurrentPosition(motionEvent.getX(i), motionEvent.getY(i));
        }
        break;
      case MotionEvent.ACTION_UP:
        boolean correctlySwiped = fingerPositions.size() == numberOfFingers;
        for (int i = 0; i < fingerPositions.size() && correctlySwiped; i++) {
          if (!fingerPositions.valueAt(i).movedInDirectionPastThreshold(direction)) {
            correctlySwiped = false;
          }
        }
        if (correctlySwiped) {
          remixerFragment.showRemixer(fragmentManager, RemixerFragment.REMIXER_TAG);
        }
        // fall-through
      case MotionEvent.ACTION_CANCEL:
        fingerPositions.clear();
        break;
      case MotionEvent.ACTION_POINTER_UP:
        break;
    }
    return true;
  }

  /**
   * Simple struct that represents the finger position, including a value for where it used to be
   * when it first touched the screen.
   */
  private static class FingerPosition {
    private final float initialX;
    private final float initialY;
    private float currentX;
    private float currentY;

    private FingerPosition(float initialX, float initialY) {
      this.initialX = initialX;
      this.initialY = initialY;
      currentX = initialX;
      currentY = initialY;
    }

    public void setCurrentPosition(float currentX, float currentY) {
      this.currentX = currentX;
      this.currentY = currentY;
    }

    /**
     * Checks whether this finger has moved at least {@link #THRESHOLD} in the correct direction.
     */
    public boolean movedInDirectionPastThreshold(Direction direction) {
      return direction.checkMovement(currentX - initialX, currentY - initialY, THRESHOLD);
    }
  }
}
