package com.onehundredtwentyninth.rangiffler.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInput {

  private String firstname;
  private String surname;
  private String avatar;
  private GqlCountry location;
}
