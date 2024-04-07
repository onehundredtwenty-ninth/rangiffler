package com.onehundredtwentyninth.rangiffler.exception;

public class FriendshipRequestNotFoundException extends IllegalArgumentException {

  public FriendshipRequestNotFoundException(String requester, String addressee) {
    super("Friendship request from " + requester + " to " + addressee + " not found");
  }
}
