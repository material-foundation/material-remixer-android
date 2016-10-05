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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import com.google.android.libraries.remixer.ui.R;

/**
 * Shows a single color as a filled circle. If {@code getColorItem().isSelected()} returns
 * {@code true} then a well contrasted check mark is displayed on top of the color circle.
 */
public class SingleColorDrawable extends Drawable {

  private final int borderWidth;
  private final int minSizeInPx;

  private final Context context;
  private final Paint paintBorder = new Paint();
  private final Paint paintFill = new Paint();

  private Drawable checkMark;
  private ColorItem colorItem;

  public SingleColorDrawable(Context context) {
    this.context = context;
    minSizeInPx =
        context.getResources().getDimensionPixelSize(R.dimen.remixerColorCircleMinimumSize);
    borderWidth = context.getResources().getDimensionPixelSize(R.dimen.remixerColorCircleBorder);
    paintBorder.setColor(Color.DKGRAY);
    paintBorder.setStyle(Paint.Style.STROKE);
    paintBorder.setStrokeWidth(borderWidth);
    paintBorder.setAntiAlias(true);
    paintFill.setStyle(Paint.Style.FILL);
    paintFill.setAntiAlias(true);
    setBounds(0, 0, minSizeInPx, minSizeInPx);
  }

  @Override
  public void setBounds(@NonNull Rect bounds) {
    if (bounds.height() < minSizeInPx || bounds.width() < minSizeInPx) {
      return;
    }
    super.setBounds(bounds);
  }

  @Override
  public void setAlpha(int i) {
    // Ignore alpha values.
  }

  @Override
  public void setColorFilter(ColorFilter colorFilter) {
    // Ignore color filters.
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  public void setColorItem(ColorItem colorItem) {
    this.colorItem = colorItem;
    double contrastRatioWithBlack =
        ColorUtils.calculateContrast(Color.BLACK, colorItem.getColor());
    double contrastRatioWithWhite =
        ColorUtils.calculateContrast(Color.WHITE, colorItem.getColor());
    @DrawableRes int checkMarkId = contrastRatioWithBlack > contrastRatioWithWhite
        ? R.drawable.ic_done_black_24 : R.drawable.ic_done_white_24;
    checkMark = ContextCompat.getDrawable(context, checkMarkId);
  }

  public ColorItem getColorItem() {
    return colorItem;
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    if (colorItem == null) {
      // should not happen, but do not crash.
      return;
    }
    int size = Math.min(getBounds().height(), getBounds().width());
    // Assume height and width are the same after onMeasure.
    float radius = size / 2f;
    // Painting a circle is fun! If filled, the circle reaches exactly to radius, but if using a
    // stroke then the stroke is half inside of the radius and half outside, hence the funny
    // calculations.
    // Paint a black outline.
    canvas.drawCircle(radius, radius, radius - borderWidth / 2f, paintBorder);
    // Paint the colored circle.
    paintFill.setColor(colorItem.getColor());
    canvas.drawCircle(radius, radius, radius - borderWidth / 2f, paintFill);
    if (colorItem.isSelected()) {
      int iconHeight = checkMark.getIntrinsicHeight();
      int iconWidth = checkMark.getIntrinsicWidth();
      // Assume that size is bigger than the checkmark, I'm guaranteeing that by using 24dp icons
      // and making the minimum size 40dp.
      int x = (size - iconWidth) / 2;
      int y = (size - iconHeight) / 2;
      checkMark.setBounds(x, y, x + iconWidth, y + iconHeight);
      checkMark.draw(canvas);
    }
  }
}
