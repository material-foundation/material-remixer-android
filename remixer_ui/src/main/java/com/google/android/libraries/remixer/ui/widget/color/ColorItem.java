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

package com.google.android.libraries.remixer.ui.widget.color;

import android.support.annotation.ColorInt;

/**
 * This class represents a color to be displayed by {@link SingleColorDrawable}. If selected
 * a check mark will be shown on top of the color disk.
 */
public class ColorItem {

  private int color;

  private boolean selected;

  public ColorItem(@ColorInt int color, boolean selected) {
    this.color = color;
    this.selected = selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public @ColorInt int getColor() {
    return color;
  }

  public boolean isSelected() {
    return selected;
  }
}
