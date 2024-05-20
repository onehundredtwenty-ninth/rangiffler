package com.onehundredtwentyninth.rangiffler.model.gql;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlFeed extends GqlResponseType {

  private String username;
  private Boolean withFriends;
  private GqlConnection<GqlPhoto> photos;
  private List<GqlStat> stat;
}
