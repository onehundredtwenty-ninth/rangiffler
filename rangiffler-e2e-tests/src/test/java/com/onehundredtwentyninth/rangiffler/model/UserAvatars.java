package com.onehundredtwentyninth.rangiffler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAvatars {

  DEFAULT("defaultAvatar.png"),
  BEE("bee.png");

  private final String fileName;
}
