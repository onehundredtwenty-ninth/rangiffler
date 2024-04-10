package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlCountryResponse extends GqlResponse<GqlCountryResponse> {

  private List<GqlCountry> countries;
}
