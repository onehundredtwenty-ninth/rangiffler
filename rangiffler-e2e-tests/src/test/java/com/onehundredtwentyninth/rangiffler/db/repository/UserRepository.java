package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.UserAuthEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import java.util.UUID;

public interface UserRepository {

  UserAuthEntity createInAuth(UserAuthEntity user);

  UserEntity createInUserdata(UserEntity user);

  void deleteInAuthById(UUID id);

  void deleteInAuthByUsername(String username);

  void deleteInUserdataById(UUID id);

  Integer count();

  UserEntity findById(UUID id);

  UserEntity findByUsername(String username);

  UserEntity findByFirstname(String firstname);

  UserEntity findByLastname(String lastName);
}
