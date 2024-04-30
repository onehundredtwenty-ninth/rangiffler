package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TestUser {

  private UUID id;
  private String username;
  private String firstname;
  private String lastName;
  private byte[] avatar;
  private TestCountry country;
  private List<TestUser> friends;
  private List<TestPhoto> photos;
  @JsonIgnore
  private TestData testData;
}
