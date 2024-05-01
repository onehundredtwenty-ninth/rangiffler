package com.onehundredtwentyninth.rangiffler.config;

import com.codeborne.selenide.Configuration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalConfig implements Config {

  static final LocalConfig INSTANCE = new LocalConfig();

  static {
    Configuration.baseUrl = "http://127.0.0.1:3001";
    Configuration.browserSize = "1920x1080";
  }

  @Override
  public String authUrl() {
    return "http://127.0.0.1:9000";
  }

  @Override
  public String gatewayUrl() {
    return "http://127.0.0.1:8080";
  }

  @Override
  public String geoHost() {
    return "localhost";
  }

  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3001";
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
