package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import java.util.UUID;

public interface UserService {

  TestUser createTestUser(CreateUser userParameters);

  TestUser createUser(String username, String password, String firstname, String lastname);

  void deleteUser(UUID id, String username);

  TestUser createRandomUser();

  void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending);

  TestUser createFriend(UUID userId, Friend friendParameters);
}
