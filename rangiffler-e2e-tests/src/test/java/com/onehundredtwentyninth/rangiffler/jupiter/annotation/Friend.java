package com.onehundredtwentyninth.rangiffler.jupiter.annotation;

public @interface Friend {

  boolean pending() default false;

  FriendshipRequestType friendshipRequestType() default FriendshipRequestType.INCOME;

  WithPhoto[] photos() default {};

  enum FriendshipRequestType {
    INCOME, OUTCOME
  }
}
