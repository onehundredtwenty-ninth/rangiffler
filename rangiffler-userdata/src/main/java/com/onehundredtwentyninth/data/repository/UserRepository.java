package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.UserEntity;
import jakarta.annotation.Nullable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  @Nullable
  UserEntity findByUsername(String username);
}
