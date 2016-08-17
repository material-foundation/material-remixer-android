package com.google.android.libraries.remixer;

/**
 * This exception is thrown if a two remixes are added with the same key.
 */
public class DuplicateRemixKeyException extends RuntimeException {
  public DuplicateRemixKeyException(String message) {
    super(message);
  }
}
