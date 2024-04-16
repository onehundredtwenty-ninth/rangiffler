package com.onehundredtwentyninth.rangiffler.test.unit;

import com.onehundredtwentyninth.rangiffler.jupiter.CreateExtrasUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({CreateUserExtension.class, CreateExtrasUserExtension.class})
class CreateUserTest {

  @CreateUser(username = "bee48", password = "123")
  @Test
  void createUserTest(TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertEquals("bee48", user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cca3bae-f195-11ee-9b32-0242ac110002", user.getCountryId().toString())
    );
  }

  @CreateUser
  @Test
  void createRandomUserTest(TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cca3bae-f195-11ee-9b32-0242ac110002", user.getCountryId().toString())
    );
  }

  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void createRandomUserWithFriendsTest(TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getFriends()),
        () -> Assertions.assertEquals(2, user.getFriends().size()),
        () -> Assertions.assertNotNull(user.getFriends().get(0)),
        () -> Assertions.assertNotNull(user.getFriends().get(1)),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getId()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getUsername()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getFirstname()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getLastName())
    );
  }

  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME)
      }
  )
  @Test
  void createRandomUserWithFriendsRequestsTest(TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getFriends()),
        () -> Assertions.assertEquals(2, user.getFriends().size()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getId()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getUsername()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getFirstname()),
        () -> Assertions.assertNotNull(user.getFriends().get(0).getLastName())
    );
  }

  @CreateUser(photos = {
      @WithPhoto(countryCode = "cn", image = "France.png", description = "insertedDescription", likes = 1),
      @WithPhoto(countryCode = "ca", image = "Amsterdam.png", description = "insertedDescription2", likes = 2)
  })
  @Test
  void createRandomUserWithPhotoTest(TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getPhotos()),
        () -> Assertions.assertEquals(2, user.getPhotos().size()),
        () -> Assertions.assertNotNull(user.getPhotos().get(0).getId()),
        () -> Assertions.assertNotNull(user.getPhotos().get(0).getSrc()),
        () -> Assertions.assertNotNull(user.getPhotos().get(0).getCountryId()),
        () -> Assertions.assertEquals("insertedDescription", user.getPhotos().get(0).getDescription()),
        () -> Assertions.assertNotNull(user.getPhotos().get(0).getCreationDate()),
        () -> Assertions.assertNotNull(user.getPhotos().get(0).getLikes()),
        () -> Assertions.assertEquals(1, user.getPhotos().get(0).getLikes().getTotal()),
        () -> Assertions.assertEquals(2, user.getPhotos().get(1).getLikes().getTotal()),
        () -> Assertions.assertNotNull(user.getPhotos().get(1).getLikes().getLikesList().get(0).getUserId())
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
  void createRandomUserWithFriendsPhotoTest(TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getFriends().get(0).getPhotos()),
        () -> Assertions.assertEquals(1, user.getPhotos().size()),
        () -> Assertions.assertEquals(1, user.getFriends().get(0).getPhotos().size()),
        () -> Assertions.assertEquals(1, user.getFriends().get(1).getPhotos().size()),
        () -> Assertions.assertNotNull(user.getFriends().get(1).getPhotos().get(0).getId()),
        () -> Assertions.assertNotNull(user.getFriends().get(1).getPhotos().get(0).getSrc()),
        () -> Assertions.assertNotNull(user.getFriends().get(1).getPhotos().get(0).getCountryId()),
        () -> Assertions.assertEquals("insertedDescriptionFriend2",
            user.getFriends().get(1).getPhotos().get(0).getDescription()),
        () -> Assertions.assertNotNull(user.getFriends().get(1).getPhotos().get(0).getCreationDate())
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
  void createRandomUserWithFriendsPhotoAndExtrasTest(TestUser user, @Extras TestUser[] extras) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(extras),
        () -> Assertions.assertEquals(2, extras.length),
        () -> Assertions.assertNotNull(user.getFriends()),
        () -> Assertions.assertEquals(2, user.getFriends().size()),
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName())
    );
  }
}
