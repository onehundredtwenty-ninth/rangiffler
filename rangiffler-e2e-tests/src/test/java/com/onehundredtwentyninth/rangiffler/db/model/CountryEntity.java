package com.onehundredtwentyninth.rangiffler.db.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryEntity {

  private UUID id;
  private String code;
  private String name;
  private byte[] flag;
}
