package com.onehundredtwentyninth.rangiffler.model.gql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlUserInput {

  private String firstname;
  private String surname;
  private String avatar;
  private GqlCountry location;
}
