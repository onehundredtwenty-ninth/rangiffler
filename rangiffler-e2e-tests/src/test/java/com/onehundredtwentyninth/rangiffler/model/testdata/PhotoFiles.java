package com.onehundredtwentyninth.rangiffler.model.testdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoFiles {

  AMSTERDAM("Amsterdam.png"),
  FRANCE("France.png");

  private final String fileName;
}
