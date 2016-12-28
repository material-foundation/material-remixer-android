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

/**
 * Contains the Remixer annotations that you can use to generate all the boilerplate required to
 * initialize Remixes.
 *
 * These annotations can only be used inside a public or default-access top-level class (No nested
 * classes) that extends android.app.Activity.
 *
 * If you decide to use Remixer Annotations in any class you mus call {@link
 * com.google.android.libraries.remixer.annotation.RemixerBinder#bind(java.lang.Object)} from your
 * {@code onCreate(...)} passing the activity, this will set up Remixer for you.
 *
 * All of the other Remixer Method annotations in this package can be applied to any public or
 * default-access non-abstract instance method that takes one parameter of the right type.
 * They all have sensible, convenient defaults explained in their own documentation.
 *
 * You can control the order in which remixes are added by moving the annotated methods in code,
 * Remixer will respect the order in which the methods are defined.
 *
 * It is possible to use both annotations and explicit Variable instantiations, you just have to add
 * your explicit variable instantiations after the call to RemixerBinder.bind.
 */
package com.google.android.libraries.remixer.annotation;
