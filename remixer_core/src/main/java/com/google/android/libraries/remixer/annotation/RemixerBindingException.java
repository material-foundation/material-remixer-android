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

/**
 * Unchecked exception that is thrown when the programmer attempts to bind an activity to remixer
 * without using any Remixer annotations in the activity.
 */
public class RemixerBindingException extends RuntimeException {

  /**
   * Advice to append at the end of messages.
   */
  private static final String ADVICE =
      ". Make sure that you're using com.google.android.libraries.remixer.annotation.*Method"
          + " annotations in this class, are not proguarding out this class' binder. Normally"
          + " adding this to your proguard configuration should help: "
          + "-keep class * extends "
          + "com.google.google.android.libraries.remixer.annotation.RemixerBinder$Binder";

  public RemixerBindingException(String message, Throwable ex) {
    super(message + ADVICE, ex);
  }
}
