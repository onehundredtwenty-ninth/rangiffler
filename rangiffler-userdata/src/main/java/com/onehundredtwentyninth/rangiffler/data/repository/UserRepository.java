package com.onehundredtwentyninth.rangiffler.data.repository;

import com.onehundredtwentyninth.rangiffler.data.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsername(String username);

  @Query("select u "
      + "from UserEntity u "
      + "where u.username != :username "
      + "and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findByUsernameNotAndSearchQuery(String username, Pageable pageable, String searchQuery);

  @Query("select u from UserEntity u "
      + "join FriendshipEntity f on u = f.addressee or u = f.requester "
      + "where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED "
      + "and (f.requester = :user or f.addressee = :user) "
      + "and u != :user "
      + "and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findFriends(UserEntity user, Pageable pageable, String searchQuery);

  @Query("select u from UserEntity u "
      + "join FriendshipEntity f on u = f.addressee "
      + "where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING "
      + "and f.requester = :requester "
      + "and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findOutcomeInvitations(UserEntity requester, Pageable pageable, String searchQuery);

  @Query("select u from UserEntity u "
      + "join FriendshipEntity f on u = f.requester "
      + "where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING "
      + "and f.addressee = :addressee "
      + "and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findIncomeInvitations(UserEntity addressee, Pageable pageable, String searchQuery);

  @Query("select u.id from UserEntity u "
      + "join FriendshipEntity f on u = f.addressee or u = f.requester "
      + "where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED "
      + "and (f.requester = :user or f.addressee = :user) "
      + "and u != :user")
  List<UUID> findFriendsIds(UserEntity user);
}
