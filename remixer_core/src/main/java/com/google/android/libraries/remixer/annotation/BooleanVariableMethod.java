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
 * Annotation to apply to a method to turn it into a Variable&lt;Boolean&gt;.
 *
 * <p>Note: It has to be used on a public or default-access method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BooleanVariableMethod {

  /**
   * The key for the variable, may be left empty and the method name will be used instead.
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
   * The initial value for this variable, assumes false as if unset.
   */
  boolean initialValue() default false;

  /**
   * The layout id to inflate when displaying this Variable. If not specified a default will be
   * used.
   *
   * <p>Its root element must implement {@code RemixerItemWidget&lt;Variable&lt;Boolean&gt;&gt;}.
   */
  int layoutId() default 0;
}
