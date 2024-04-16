package com.onehundredtwentyninth.rangiffler.service;

import com.github.javafaker.Faker;
import com.onehundredtwentyninth.rangiffler.db.model.Authority;
import com.onehundredtwentyninth.rangiffler.db.model.AuthorityEntity;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.model.UserAuthEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.grpc.Like;
import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDbService implements UserService {

  private final UserRepository userRepository = new UserRepositorySJdbc();
  private final FriendshipRepository friendshipRepository = new FriendshipRepositorySJdbc();
  private final PhotoTestService photoService = new PhotoDbService();
  private final Faker faker = new Faker();

  @Override
  public TestUser createUser(String username, String password, String firstname, String lastname) {
    var userAuth = new UserAuthEntity();
    userAuth.setUsername(username);
    userAuth.setPassword(password);
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    var authorities = Arrays.stream(Authority.values()).map(
        a -> {
          var ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toList();

    userAuth.setAuthorities(authorities);

    var userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setLastName(lastname);
    userEntity.setFirstname(firstname);
    userEntity.setCountryId(UUID.fromString("4cca3bae-f195-11ee-9b32-0242ac110002"));

    userRepository.createInAuth(userAuth);
    userEntity = userRepository.createInUserdata(userEntity);
    log.info("Создан пользователь с id {}", userEntity.getId());

    return UserEntityMapper.toUser(userEntity);
  }

  @Override
  public void deleteUser(UUID id, String username) {
    userRepository.deleteInAuthByUsername(username);
    userRepository.deleteInUserdataById(id);
  }

  @Override
  public TestUser createRandomUser() {
    var faker = new Faker();
    return createUser(faker.name().username(), "123", faker.name().firstName(), faker.name().lastName());
  }

  @Override
  public void createFriendship(UUID firstFriendId, UUID secondFriendId, Boolean isPending) {
    friendshipRepository.createFriendship(
        firstFriendId,
        secondFriendId,
        LocalDateTime.now(),
        isPending ? FriendshipStatus.PENDING : FriendshipStatus.ACCEPTED
    );
  }

  @Override
  public TestUser createFriend(UUID userId, Friend friendParameters) {
    var createdFriend = createRandomUser();

    if (!friendParameters.pending()) {
      createFriendship(userId, createdFriend.getId(), false);
    } else {
      if (friendParameters.friendshipRequestType() == FriendshipRequestType.OUTCOME) {
        createFriendship(userId, createdFriend.getId(), true);
      } else {
        createFriendship(createdFriend.getId(), userId, true);
      }
    }
    return createdFriend;
  }

  @Override
  public TestUser createTestUser(CreateUser userParameters) {
    var createdUser = userParameters.username().isEmpty()
        ? createRandomUser()
        : createUser(userParameters.username(), userParameters.password(), faker.name().firstName(),
            faker.name().lastName());

    var friends = new ArrayList<TestUser>();
    var createdPhotos = new ArrayList<Photo>();

    for (var photoParameters : userParameters.photos()) {
      var createdPhoto = photoService.createPhoto(createdUser.getId(), photoParameters.countryCode(),
          photoParameters.image(), photoParameters.description());

      var likes = new ArrayList<Like>();
      for (var i = 0; i < photoParameters.likes(); i++) {
        var likeUser = createRandomUser();
        photoService.likePhoto(likeUser.getId(), UUID.fromString(createdPhoto.getId()));
        likes.add(Like.newBuilder().setUserId(likeUser.getId().toString()).build());
      }
      createdPhoto = createdPhoto.toBuilder()
          .setLikes(Likes.newBuilder().setTotal(likes.size()).addAllLikes(likes).build()).build();
      createdPhotos.add(createdPhoto);
    }

    for (var friendParameters : userParameters.friends()) {
      var createdFriend = createFriend(createdUser.getId(), friendParameters);
      friends.add(createdFriend);

      for (var photoParameters : friendParameters.photos()) {
        var createdPhoto = photoService.createPhoto(createdFriend.getId(),
            photoParameters.countryCode(), photoParameters.image(), photoParameters.description());

        var likes = new ArrayList<Like>();
        for (var i = 0; i < photoParameters.likes(); i++) {
          var likeUser = createRandomUser();
          photoService.likePhoto(likeUser.getId(), UUID.fromString(createdPhoto.getId()));
          likes.add(Like.newBuilder().setUserId(likeUser.getId().toString()).build());
        }
        createdPhoto = createdPhoto.toBuilder()
            .setLikes(Likes.newBuilder().setTotal(likes.size()).addAllLikes(likes).build()).build();
        createdPhotos.add(createdPhoto);
      }
    }

    createdUser.getFriends().addAll(friends);
    createdUser.getPhotos().addAll(createdPhotos);
    return createdUser;
  }
}
