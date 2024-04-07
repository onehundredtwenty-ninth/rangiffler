package com.onehundredtwentyninth.rangiffler.exception;

public class PhotoNotFoundException extends IllegalArgumentException {

  public PhotoNotFoundException(String photoId) {
    super("Photo with id " + photoId + " not found");
  }
}
