package com.onehundredtwentyninth.rangiffler.db.model;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeEntity {

  private UUID id;
  private UUID userId;
  private Timestamp createdDate;
}
