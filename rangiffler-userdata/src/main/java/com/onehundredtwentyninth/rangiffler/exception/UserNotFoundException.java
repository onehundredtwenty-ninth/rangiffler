package com.onehundredtwentyninth.rangiffler.exception;

public class UserNotFoundException extends IllegalArgumentException {

  public UserNotFoundException(String userAttribute) {
    super("User " + userAttribute + " not found");
  }
}
