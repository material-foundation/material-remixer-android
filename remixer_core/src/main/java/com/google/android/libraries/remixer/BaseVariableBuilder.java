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

package com.google.android.libraries.remixer;

/**
 * The base class for all variable builders
 * @param <V> The type of variable this builder builds.
 * @param <T> The type of data stored in the variable.
 */
public abstract class BaseVariableBuilder<V, T> {
  protected T defaultValue;
  protected String key;
  protected String title;
  protected Object context;
  protected int layoutId = 0;
  protected Callback<T> callback;
  protected DataType dataType;

  public BaseVariableBuilder<V, T> setKey(String key) {
    this.key = key;
    return this;
  }

  public BaseVariableBuilder<V, T> setTitle(String title) {
    this.title = title;
    return this;
  }

  public BaseVariableBuilder<V, T> setLayoutId(int layoutId) {
    this.layoutId = layoutId;
    return this;
  }

  public BaseVariableBuilder<V, T> setContext(Object context) {
    this.context = context;
    return this;
  }

  public BaseVariableBuilder<V, T> setCallback(Callback<T> callback) {
    this.callback = callback;
    return this;
  }

  public BaseVariableBuilder<V, T> setDataType(DataType dataType) {
    this.dataType = dataType;
    return this;
  }

  public BaseVariableBuilder<V, T> setDefaultValue(T defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  protected void checkBaseFields() {
    if (key == null) {
      throw new IllegalArgumentException("key cannot be unset");
    }
    if (context == null) {
      throw new IllegalArgumentException("context cannot be unset");
    }
    if (dataType == null) {
      throw new IllegalArgumentException("dataType cannot be unset");
    }
    if (title == null) {
      title = key;
    }
  }

  /**
   * Returns the built Variable. Implementors must call {@link #checkBaseFields()}.
   * @throws IllegalArgumentException if the minimally required fields were not set or their
   *     configuration is invalid.
   */
  public abstract V build();
}
