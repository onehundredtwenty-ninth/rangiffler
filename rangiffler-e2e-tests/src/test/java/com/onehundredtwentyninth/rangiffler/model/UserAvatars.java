package com.onehundredtwentyninth.rangiffler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAvatars {

  BEE("bee.jpg");

  private final String fileName;
}
