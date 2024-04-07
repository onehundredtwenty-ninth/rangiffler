package com.onehundredtwentyninth.rangiffler.exception;

import java.util.NoSuchElementException;

public class PhotoNotFoundException extends NoSuchElementException {

  public PhotoNotFoundException(String photoId) {
    super("Photo with id " + photoId + " not found");
  }
}
