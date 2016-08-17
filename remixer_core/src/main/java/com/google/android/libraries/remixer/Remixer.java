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
   * This adds a remix to be tracked and displayed to the user
   *
   * @param remix The remix to be added
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
