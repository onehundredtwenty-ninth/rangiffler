package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsername(String username);

  @Query("select u from UserEntity u where u.username != :username" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findByUsernameNotAndSearchQuery(@Param("username") String username,
      @Nonnull Pageable pageable,
      @Param("searchQuery") String searchQuery);
}
