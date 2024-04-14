package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;

public interface UserService {

  User createUser(String username, String password, String firstname, String lastname);

  void deleteUser(User user);

  User createRandomUser();

  void createFriendship(String firstFriendId, String secondFriendId, Boolean isPending);

  User createFriend(String userId, Friend friendParameters);
}
