package com.onehundredtwentyninth.rangiffler.test.unit;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateExtrasUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.Friends;
import com.onehundredtwentyninth.rangiffler.jupiter.WithPhoto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({CreateUserExtension.class, CreateExtrasUserExtension.class})
class CreateUserTest {

  @CreateUser(username = "bee48", password = "123")
  @Test
  void createUserTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertEquals("bee48", user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cca3bae-f195-11ee-9b32-0242ac110002", user.getCountryId())
    );
  }

  @CreateUser
  @Test
  void createRandomUserTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cca3bae-f195-11ee-9b32-0242ac110002", user.getCountryId())
    );
  }

  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void createRandomUserWithFriendsTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName())
    );
  }

  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME)
      }
  )
  @Test
  void createRandomUserWithFriendsRequestsTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName())
    );
  }

  @CreateUser(photos = {
      @WithPhoto(countryCode = "cn", image = "France.png", description = "insertedDescription", likes = 1),
      @WithPhoto(countryCode = "ca", image = "Amsterdam.png", description = "insertedDescription2", likes = 2)
  })
  @Test
  void createRandomUserWithPhotoTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName())
    );
  }

  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME,
              photos = {
                  @WithPhoto(countryCode = "mx", image = "France.png", description = "insertedDescriptionFriend"),
              }),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME,
              photos = {
                  @WithPhoto(countryCode = "ca", image = "Amsterdam.png", description = "insertedDescriptionFriend2"),
              })
      },
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png", description = "insertedDescription"),
      })
  @Test
  void createRandomUserWithFriendsPhotoTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName())
    );
  }

  @CreateExtrasUsers({@CreateUser, @CreateUser})
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME,
              photos = {
                  @WithPhoto(countryCode = "mx", image = "France.png", description = "insertedDescriptionFriend"),
              }),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME,
              photos = {
                  @WithPhoto(countryCode = "ca", image = "Amsterdam.png", description = "insertedDescriptionFriend2"),
              })
      },
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png", description = "insertedDescription"),
      })
  @Test
  void createRandomUserWithFriendsPhotoAndExtrasTest(User user, @Friends User[] friends, @Extras User[] extras) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(extras),
        () -> Assertions.assertEquals(2, extras.length),
        () -> Assertions.assertNotNull(friends),
        () -> Assertions.assertEquals(2, friends.length),
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName())
    );
  }
}
