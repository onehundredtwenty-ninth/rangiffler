package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestSlice<T> extends SliceImpl<T> {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public RestSlice(@JsonProperty("edges") List<T> edges, @JsonProperty("pageInfo") GqlPageInfo pageInfo) {
    super(edges, PageRequest.ofSize(edges.size()), pageInfo.hasNextPage());
  }
}
