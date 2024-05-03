package com.onehundredtwentyninth.rangiffler.model.gql;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GqlLikes extends GqlResponseType {

  private Integer total;
  private List<GqlLike> likes;
}
