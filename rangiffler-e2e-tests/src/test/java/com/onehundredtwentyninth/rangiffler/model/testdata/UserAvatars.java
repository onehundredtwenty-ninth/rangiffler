package com.onehundredtwentyninth.rangiffler.model.testdata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAvatars {

  DEFAULT("defaultAvatar.png"),
  BEE("bee.png");

  private final String fileName;
}
