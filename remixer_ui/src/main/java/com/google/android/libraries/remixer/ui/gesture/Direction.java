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

/**
 * Describes a movement's direction, and contains a helper method to check if that movement is
 * roughly in that direction.
 */
public enum Direction {
  UP(0, -1),
  DOWN(0, 1),
  LEFT(-1, 0),
  RIGHT(1, 0);

  /**
   * Multiplier to use on the X coordinates when calculating movement direction.
   */
  private final int xMultiplier;
  /**
   * Multiplier to use on the Y coordinates when calculating movement direction.
   */
  private final int yMultiplier;

  Direction(int xMultiplier, int yMultiplier) {
    this.xMultiplier = xMultiplier;
    this.yMultiplier = yMultiplier;
  }

  /**
   * Checks that the movement described by {@code deltaX} and {@code deltaY} is roughly in this
   * direction and that it moved at least {@code threshold}.
   */
  public boolean checkMovement(float deltaX, float deltaY, float threshold) {
    deltaX *= xMultiplier;
    deltaY *= yMultiplier;
    return deltaX + deltaY > threshold;
  }
}
