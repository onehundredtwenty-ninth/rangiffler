package com.onehundredtwentyninth.rangiffler.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalConfig implements Config {

  static final LocalConfig INSTANCE = new LocalConfig();

  @Override
  public String authUrl() {
    return "http://127.0.0.1:9000";
  }

  @Override
  public String gatewayUrl() {
    return "http://127.0.0.1:8080";
  }

  @Override
  public String geoUrl() {
    return "http://127.0.0.1:8091";
  }

  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3001";
  }

  @Override
  public String photoUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String userdataUrl() {
    return "http://127.0.0.1:8092";
  }

  @Override
  public String jdbcHost() {
    return "localhost";
  }
}
