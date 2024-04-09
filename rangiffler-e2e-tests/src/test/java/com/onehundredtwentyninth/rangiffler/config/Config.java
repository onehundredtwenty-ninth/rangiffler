package com.onehundredtwentyninth.rangiffler.config;

public interface Config {

  static Config getInstance() {
    return "docker".equals(System.getProperty("test.env"))
        ? DockerConfig.INSTANCE
        : LocalConfig.INSTANCE;
  }

  String authUrl();

  String gatewayUrl();

  String geoUrl();

  String frontUrl();

  String photoUrl();

  String userdataUrl();

  String jdbcHost();

  default String jdbcUser() {
    return "postgres";
  }

  default String jdbcPassword() {
    return "secret";
  }

  default int jdbcPort() {
    return 5432;
  }
}
