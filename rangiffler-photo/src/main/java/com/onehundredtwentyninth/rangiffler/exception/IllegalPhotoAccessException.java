package com.onehundredtwentyninth.rangiffler.exception;

public class IllegalPhotoAccessException extends IllegalStateException {

  public IllegalPhotoAccessException(String photoId, String userId) {
    super("Photo with id " + photoId + " can't be modified by user " + userId);
  }
}
