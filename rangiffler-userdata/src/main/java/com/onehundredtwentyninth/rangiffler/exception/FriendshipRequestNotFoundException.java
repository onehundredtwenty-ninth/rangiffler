package com.onehundredtwentyninth.rangiffler.exception;

import java.util.NoSuchElementException;

public class FriendshipRequestNotFoundException extends NoSuchElementException {

  public FriendshipRequestNotFoundException(String requester, String addressee) {
    super("Friendship request from " + requester + " to " + addressee + " not found");
  }
}
