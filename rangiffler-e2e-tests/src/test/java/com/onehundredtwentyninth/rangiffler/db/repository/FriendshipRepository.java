package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public interface FriendshipRepository {

  void createFriendship(UUID firstFriendId, UUID secondFriendId, LocalDateTime createdDate,
      FriendshipStatus friendshipStatus);
}
