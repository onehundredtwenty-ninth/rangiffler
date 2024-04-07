package com.onehundredtwentyninth.rangiffler.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {

  public UserNotFoundException(String userAttribute) {
    super("User " + userAttribute + " not found");
  }
}
