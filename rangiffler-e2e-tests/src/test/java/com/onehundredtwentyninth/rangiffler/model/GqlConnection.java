package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlConnection<T> extends GqlResponseType {

  private List<T> edges;
  private GqlPageInfo pageInfo;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public GqlConnection(@JsonProperty("edges") List<T> edges, @JsonProperty("pageInfo") GqlPageInfo pageInfo) {
    this.edges = edges;
    this.pageInfo = pageInfo;
  }
}
