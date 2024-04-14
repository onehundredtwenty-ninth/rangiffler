package com.onehundredtwentyninth.rangiffler.db.model;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipEntity {

  private UUID id;
  private UUID requesterId;
  private UUID addresseeId;
  private Timestamp createdDate;
  private FriendshipStatus status;
}
