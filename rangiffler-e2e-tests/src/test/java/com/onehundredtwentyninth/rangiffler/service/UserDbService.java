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
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDbService implements UserService {

  private final UserRepository userRepository = new UserRepositorySJdbc();
  private final FriendshipRepository friendshipRepository = new FriendshipRepositorySJdbc();

  @Override
  public User createUser(String username, String password, String firstname, String lastname) {
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

    return UserEntityMapper.toMessage(userEntity);
  }

  @Override
  public void deleteUser(User user) {
    userRepository.deleteInAuthByUsername(user.getUsername());
    userRepository.deleteInUserdataById(UUID.fromString(user.getId()));
  }

  @Override
  public User createRandomUser() {
    var faker = new Faker();
    return createUser(faker.name().username(), "123", faker.name().firstName(), faker.name().lastName());
  }

  @Override
  public void createFriendship(String firstFriendId, String secondFriendId, Boolean isPending) {
    friendshipRepository.createFriendship(
        UUID.fromString(firstFriendId),
        UUID.fromString(secondFriendId),
        LocalDateTime.now(),
        isPending ? FriendshipStatus.PENDING : FriendshipStatus.ACCEPTED
    );
  }

  @Override
  public User createFriend(String userId, Friend friendParameters) {
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
}
