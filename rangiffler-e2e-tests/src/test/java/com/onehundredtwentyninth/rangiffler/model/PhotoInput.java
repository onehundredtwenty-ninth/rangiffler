package com.onehundredtwentyninth.rangiffler.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoInput {

  private UUID id;
  private String src;
  private CountryInput country;
  private String description;
  private LikeInput like;
}
