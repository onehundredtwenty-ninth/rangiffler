package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import java.util.List;
import java.util.UUID;

public interface UserService {

  TestUser createTestUser(CreateUser userParameters);

  TestUser createUser(String username, String password, String firstname, String lastname, UUID countryId,
      byte[] avatar);

  void deleteUser(TestUser testUser);

  void deleteUser(String username);

  TestUser createRandomUser();

  void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending);

  List<TestPhoto> createPhotos(UUID userId, WithPhoto[] photosParameters);
}
