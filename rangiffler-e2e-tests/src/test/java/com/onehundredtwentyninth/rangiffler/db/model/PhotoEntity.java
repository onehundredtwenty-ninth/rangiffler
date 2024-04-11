package com.onehundredtwentyninth.rangiffler.db.model;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoEntity {

  private UUID id;
  private UUID userId;
  private UUID countryId;
  private String description;
  private byte[] photo;
  private Timestamp createdDate;
}
