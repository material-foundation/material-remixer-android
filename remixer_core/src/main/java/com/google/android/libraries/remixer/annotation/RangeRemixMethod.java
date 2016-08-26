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

package com.google.android.libraries.remixer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to apply to a method to turn it into a RangeRemix.
 *
 * <p>This is set up in a way that if no values are specified it will be a range from 0 to 100, with
 * the default value as 0. Furthermore if you only move the range so that 0 is not included it will
 * set the default to be minValue.
 *
 * <p>Note: It has to be used on a public or default-access method in the same class that has a
 * @RemixerInstance annotated field.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface RangeRemixMethod {
  /**
   * The key for the remix, may be left empty and the method name will be used instead.
   *
   * <p>If using a custom key, make sure the key is unique and that it is a valid java identifier
   * name, because it will be used as such in code.
   */
  String key() default "";

  /**
   * The title to display in the UI, may be left empty and the key will be used instead.
   */
  String title() default "";

  /**
   * The default value for this remix.
   *
   * <p>If left unspecified (0) and it is out of bounds, it will default to minValue, for
   * convenience.
   */
  int defaultValue() default 0;

  /**
   * The minimum value for this RangeRemix. It is 0 by default.
   */
  int minValue() default 0;

  /**
   * The maximum value for this RangeRemix. It is 100 by default.
   */
  int maxValue() default 100;

  /**
   * The layout id to inflate when displaying this Remix. If not specified a default will be used.
   *
   * <p>Its root element must implement {@code RemixWidget<Integer>}.
   */
  int layoutId() default 0;
}
