package com.onehundredtwentyninth.rangiffler.config;

import com.codeborne.selenide.Configuration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DockerBackendLaunchLocalConfig implements Config {

  static final DockerBackendLaunchLocalConfig INSTANCE = new DockerBackendLaunchLocalConfig();

  static {
    Configuration.baseUrl = "http://frontend.rangiffler.dc";
  }

  @Override
  public String authUrl() {
    return "http://auth.rangiffler.dc:9000";
  }

  @Override
  public String gatewayUrl() {
    return "http://gateway.rangiffler.dc:8080";
  }

  @Override
  public String geoHost() {
    return "localhost";
  }

  @Override
  public String frontUrl() {
    return "http://frontend.rangiffler.dc";
  }

  @Override
  public String photoHost() {
    return "localhost";
  }

  @Override
  public String userdataHost() {
    return "localhost";
  }

  @Override
  public String jdbcHost() {
    return "localhost";
  }
}
