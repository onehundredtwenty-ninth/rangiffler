package com.onehundredtwentyninth.rangiffler.service;

import com.github.javafaker.Faker;
import com.onehundredtwentyninth.rangiffler.db.model.Authority;
import com.onehundredtwentyninth.rangiffler.db.model.AuthorityEntity;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.model.UserAuthEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.grpc.Like;
import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.mapper.CountryMapper;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import com.onehundredtwentyninth.rangiffler.model.TestData;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.utils.ImageUtils;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDbService implements UserService {

  private final UserRepository userRepository = new UserRepositorySJdbc();
  private final FriendshipRepository friendshipRepository = new FriendshipRepositorySJdbc();
  private final CountryRepository countryRepository = new CountryRepositorySJdbc();
  private final PhotoService photoService = new PhotoDbService();
  private final Faker faker = new Faker();

  @Override
  public TestUser createUser(String username, String password, String firstname, String lastname, UUID countryId,
      byte[] avatar) {
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
    userEntity.setCountryId(countryId);
    userEntity.setAvatar(avatar);

    userRepository.createInAuth(userAuth);
    userEntity = userRepository.createInUserdata(userEntity);
    log.info("Создан пользователь с id {}", userEntity.getId());

    var testUser = UserEntityMapper.toUser(userEntity);
    testUser.setTestData(new TestData(password));
    return testUser;
  }

  @Override
  public void deleteUser(TestUser testUser) {
    testUser.getPhotos().forEach(s -> photoService.deletePhoto(UUID.fromString(s.getId())));
    testUser.getPhotos().stream()
        .flatMap(s -> s.getLikes().getLikesList().stream())
        .forEach(s -> {
          var userId = UUID.fromString(s.getUserId());
          var username = userRepository.findById(userId).getUsername();
          deleteUser(userId, username);
        });
    deleteUser(testUser.getId(), testUser.getUsername());
  }

  @Override
  public void deleteUser(UUID id, String username) {
    userRepository.deleteInAuthByUsername(username);
    userRepository.deleteInUserdataById(id);
  }

  @Override
  public TestUser createRandomUser() {
    var faker = new Faker();
    return createUser(faker.name().username(), "123", faker.name().firstName(), faker.name().lastName(),
        UUID.fromString("4cca3bae-f195-11ee-9b32-0242ac110002"), new byte[]{});
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
    var username = userParameters.username().isEmpty() ? faker.name().username() : userParameters.username();
    var password = userParameters.password().isEmpty() ? faker.internet().password() : userParameters.password();
    var userCountry = countryRepository.findCountryByCode(userParameters.countryCode().getCode());
    var userAvatar = ImageUtils.getImageFromResourceAsBase64(userParameters.avatar().getFileName());

    var createdUser = createUser(username, password, faker.name().firstName(), faker.name().lastName(),
        userCountry.getId(), userAvatar.getBytes(StandardCharsets.UTF_8));
    createdUser.setCountry(CountryMapper.toTestCountry(userCountry));

    createdUser.getPhotos().addAll(createPhotos(createdUser.getId(), userParameters.photos()));

    var friends = new ArrayList<TestUser>();
    for (var friendParameters : userParameters.friends()) {
      var createdFriend = createFriend(createdUser.getId(), friendParameters);
      friends.add(createdFriend);
      createdFriend.getPhotos().addAll(createPhotos(createdFriend.getId(), friendParameters.photos()));
    }
    createdUser.getFriends().addAll(friends);

    return createdUser;
  }

  @Override
  public List<Photo> createPhotos(UUID userId, WithPhoto[] photosParameters) {
    var createdPhotos = new ArrayList<Photo>();
    for (var photoParameters : photosParameters) {
      var createdPhoto = photoService.createPhoto(userId, photoParameters.countryCode(), photoParameters.image(),
          photoParameters.description());

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
    return createdPhotos;
  }
}
