package com.onehundredtwentyninth.rangiffler.db.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

  private UUID id;
  private String username;
  private String firstname;
  private String lastName;
  private byte[] avatar;
  private UUID countryId;
}
