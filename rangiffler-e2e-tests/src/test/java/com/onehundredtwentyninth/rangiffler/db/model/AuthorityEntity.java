package com.onehundredtwentyninth.rangiffler.db.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityEntity {

  private UUID id;
  private Authority authority;
}
