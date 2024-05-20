package com.onehundredtwentyninth.rangiffler.model.gql;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlPhotoInput {

  private UUID id;
  private String src;
  private GqlCountryInput country;
  private String description;
  private GqlLikeInput like;
}
