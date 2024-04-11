package com.onehundredtwentyninth.rangiffler.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlCountry extends GqlResponseType {

  private String code;
  private String name;
  private String flag;
}
