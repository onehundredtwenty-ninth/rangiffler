package com.onehundredtwentyninth.rangiffler.exception;

import java.util.NoSuchElementException;

public class FriendshipNotFoundException extends NoSuchElementException {

  public FriendshipNotFoundException(String actionAuthorUsername, String actionTargetUserName) {
    super("Friendship between " + actionAuthorUsername + " and " + actionTargetUserName + " not found");
  }
}
