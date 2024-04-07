package com.onehundredtwentyninth.rangiffler.data.repository;

import com.onehundredtwentyninth.rangiffler.data.UserEntity;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee or u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED and (f.requester = :user or f.addressee = :user)"
      +
      " and u != :user")
  Page<UserEntity> findFriends(@Param("user") UserEntity requester, @Nonnull Pageable pageable);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee or u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED and (f.requester = :user or f.addressee = :user)" +
      " and u != :user" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findFriends(@Param("user") UserEntity user,
      @Nonnull Pageable pageable,
      @Param("searchQuery") String searchQuery);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee or u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED and (f.requester = :user or f.addressee = :user)"
      +
      " and u != :user")
  List<UserEntity> findFriends(@Param("user") UserEntity requester);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee or u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED and (f.requester = :user or f.addressee = :user)"
      +
      " and u != :user" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Slice<UserEntity> findFriends(@Param("user") UserEntity requester, @Param("searchQuery") String searchQuery);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.requester = :requester")
  Page<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
      @Nonnull Pageable pageable);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.requester = :requester" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
      @Nonnull Pageable pageable,
      @Param("searchQuery") String searchQuery);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.requester = :requester")
  List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.requester = :requester" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
      @Param("searchQuery") String searchQuery);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.addressee = :addressee")
  Page<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
      @Nonnull Pageable pageable);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.addressee = :addressee" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  Page<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
      @Nonnull Pageable pageable,
      @Param("searchQuery") String searchQuery);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.addressee = :addressee")
  List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee);

  @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.PENDING and f.addressee = :addressee" +
      " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
  List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
      @Param("searchQuery") String searchQuery);

  @Query("select u.id from UserEntity u join FriendshipEntity f on u = f.addressee or u = f.requester" +
      " where f.status = com.onehundredtwentyninth.rangiffler.data.FriendshipStatus.ACCEPTED and (f.requester = :user or f.addressee = :user)" +
      " and u != :user")
  List<UUID> findFriendsIds(@Param("user") UserEntity userEntity);
}
