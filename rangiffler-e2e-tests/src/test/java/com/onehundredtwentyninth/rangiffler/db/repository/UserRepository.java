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

  void deleteInUserdataByUsername(String username);

  Integer count();

  UserEntity findRequiredById(UUID id);

  UserEntity findRequiredByUsername(String username);

  UserEntity findRequiredByFirstname(String firstname);

  UserEntity findRequiredByLastname(String lastName);
}
