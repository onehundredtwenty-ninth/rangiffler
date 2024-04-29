package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlUserResponse {

  @JsonAlias("friendship")
  private GqlUser user;
}
