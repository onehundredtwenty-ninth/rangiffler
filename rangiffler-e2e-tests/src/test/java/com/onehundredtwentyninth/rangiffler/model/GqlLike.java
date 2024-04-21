package com.onehundredtwentyninth.rangiffler.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlLike extends GqlResponseType {

  private UUID user;
  private String userName;
  private LocalDate creationDate;
}
