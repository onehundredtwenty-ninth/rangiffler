package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GqlResponse<T> {

  protected T data;
  protected List<GqlError> errors;
}
