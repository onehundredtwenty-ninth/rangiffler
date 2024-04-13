package com.onehundredtwentyninth.rangiffler.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DockerConfig implements Config {

  static final DockerConfig INSTANCE = new DockerConfig();

  @Override
  public String authUrl() {
    return "http://auth.rangiffler.dc:9000";
  }

  @Override
  public String gatewayUrl() {
    return "http://gateway.rangiffler.dc:8080";
  }

  @Override
  public String geoUrl() {
    return "geo.rangiffler.dc:8091";
  }

  @Override
  public String frontUrl() {
    return "http://frontend.rangiffler.dc";
  }

  @Override
  public String photoUrl() {
    return "photo.rangiffler.dc:8093";
  }

  @Override
  public String userdataUrl() {
    return "userdata.rangiffler.dc:8092";
  }

  @Override
  public String jdbcHost() {
    return "rangiffler-all-db";
  }
}
