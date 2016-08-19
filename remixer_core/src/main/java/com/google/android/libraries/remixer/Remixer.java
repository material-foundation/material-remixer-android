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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains a list of {@link Remix}es.
 */
public class Remixer {

  private HashMap<String, Remix<?>> remixMap;
  private List<Remix<?>> remixList;

  public Remixer() {
    remixMap = new HashMap<>();
    remixList = new ArrayList<>();
  }

  /**
   * This adds a remix to be tracked and displayed to the user.
   *
   * @param remix The remix to be added.
   * @throws DuplicateRemixKeyException In case the remix has a key that has already been used.
   */
  public void addRemix(Remix remix) {
    String key = remix.getKey();
    if (remixMap.containsKey(key)) {
      throw new DuplicateRemixKeyException(
          String.format(
              "Trying to add a %s as remix key %s but a %s already has that key",
              remix.getClass().getName(),
              key,
              remixMap.get(key).getClass().getName()));
    }
    remixMap.put(key, remix);
    remixList.add(remix);
  }

  public List<Remix<?>> getRemixList() {
    return remixList;
  }
}
