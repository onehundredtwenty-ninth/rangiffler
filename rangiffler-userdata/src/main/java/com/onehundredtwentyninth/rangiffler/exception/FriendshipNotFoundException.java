package com.onehundredtwentyninth.rangiffler.exception;

public class FriendshipNotFoundException extends IllegalArgumentException {

  public FriendshipNotFoundException(String actionAuthorUsername, String actionTargetUserName) {
    super("Friendship between " + actionAuthorUsername + " and " + actionTargetUserName + " not found");
  }
}
