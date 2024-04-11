package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.grpc.User;

public interface UserService {

  User createUser(String username, String password);

  void deleteUser(User user);

  User createRandomUser();

  void createFriendship(String firstFriendId, String secondFriendId, Boolean isPending);
}
