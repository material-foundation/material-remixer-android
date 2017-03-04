/*
 * Copyright 2017 Google Inc.
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

package com.google.android.libraries.remixer.serialization;

/**
 * A serialized color represents alpha, red, green and blue as 0-255 integers.
 *
 * <p>This is the agreed-upon representation of colors in Remixer. Sadly there is no uniform color
 * representation across platforms.
 */
@SuppressWarnings("checkstyle:membername")
public class SerializedColor {

  /**
   * Alpha component from 0 to 255.
   */
  private int a;

  /**
   * Red component from 0 to 255.
   */
  private int r;

  /**
   * Green component from 0 to 255.
   */
  private int g;

  /**
   * Blue component from 0 to 255.
   */
  private int b;

  public SerializedColor() {
    a = 0;
    r = 0;
    g = 0;
    b = 0;
  }

  /**
   * Initialize a SerializedColor from an android native color.
   */
  public SerializedColor(int color) {
    a = color >>> 24;
    r = (color >>> 16) & 0xFF;
    g = (color >>> 8) & 0xFF;
    b = color & 0xFF;
  }

  public int getA() {
    return a;
  }

  @SuppressWarnings("checkstyle:parametername")
  public void setA(int a) {
    this.a = a;
  }

  public int getR() {
    return r;
  }

  @SuppressWarnings("checkstyle:parametername")
  public void setR(int r) {
    this.r = r;
  }

  public int getG() {
    return g;
  }

  @SuppressWarnings("checkstyle:parametername")
  public void setG(int g) {
    this.g = g;
  }

  public int getB() {
    return b;
  }

  @SuppressWarnings("checkstyle:parametername")
  public void setB(int b) {
    this.b = b;
  }

  /**
   * Returns a native android representation of this color, as a AARRGGBB hex integer.
   */
  public int toAndroidColor() {
    return a << 24 | r << 16 | g << 8 | b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SerializedColor that = (SerializedColor) o;

    if (a != that.a) return false;
    if (r != that.r) return false;
    if (g != that.g) return false;
    return b == that.b;

  }

  @Override
  public int hashCode() {
    int result = a;
    result = 31 * result + r;
    result = 31 * result + g;
    result = 31 * result + b;
    return result;
  }
}
