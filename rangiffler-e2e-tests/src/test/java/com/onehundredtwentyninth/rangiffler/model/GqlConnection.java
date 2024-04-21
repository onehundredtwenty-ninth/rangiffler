package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlConnection<T> extends GqlResponseType {

  private List<T> edges;
  private GqlPageInfo pageInfo;
}
