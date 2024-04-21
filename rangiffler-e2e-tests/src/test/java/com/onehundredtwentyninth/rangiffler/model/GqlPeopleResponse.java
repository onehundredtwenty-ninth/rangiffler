package com.onehundredtwentyninth.rangiffler.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlPeopleResponse {

  private GqlConnection<GqlUser> users;
}
