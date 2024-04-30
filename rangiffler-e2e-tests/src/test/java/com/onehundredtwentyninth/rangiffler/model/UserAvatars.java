package com.onehundredtwentyninth.rangiffler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAvatars {

  DEFAULT("defaultAvatar.jpg"),
  BEE("bee.jpg");

  private final String fileName;
}
