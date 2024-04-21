package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlLikes extends GqlResponseType {

  private Integer total;
  private List<GqlLike> likes;
}
