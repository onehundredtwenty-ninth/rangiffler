package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.FriendshipEntity;
import com.onehundredtwentyninth.data.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, UUID> {

  Optional<FriendshipEntity> findByRequesterAndAddressee(UserEntity requester, UserEntity addressee);

  @Query("select f from FriendshipEntity f "
      + "where (f.requester = :user or f.addressee = :user) "
      + "and (f.requester = :friend or f.addressee = :friend)")
  Optional<FriendshipEntity> findFriendship(UserEntity user, UserEntity friend);
}
