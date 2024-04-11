package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.db.model.Authority;
import com.onehundredtwentyninth.rangiffler.db.model.AuthorityEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserAuthEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserDbService {

  private final UserRepository userRepository = new UserRepositorySJdbc();

  public User createUser(String username, String password) {
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
    userEntity.setLastName(username);
    userEntity.setFirstname(username);
    userEntity.setCountryId(UUID.fromString("4cca3bae-f195-11ee-9b32-0242ac110002"));

    userRepository.createInAuth(userAuth);
    userEntity = userRepository.createInUserdata(userEntity);
    log.info("Создан пользователь с id {}", userEntity.getId());

    return UserEntityMapper.toMessage(userEntity);
  }

  public void deleteUser(User user) {
    userRepository.deleteInAuthByUsername(user.getUsername());
    userRepository.deleteInUserdataById(UUID.fromString(user.getId()));
  }
}
