package com.onehundredtwentyninth.rangiffler.config;

public interface Config {

  static Config getInstance() {
    return "docker".equals(System.getProperty("test.env"))
        ? DockerConfig.INSTANCE
        : LocalConfig.INSTANCE;
  }

  String authUrl();

  String gatewayUrl();

  String geoHost();

  String frontUrl();

  String photoHost();

  String userdataHost();

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

  default int geoPort() {
    return 8091;
  }

  default int photoPort() {
    return 8093;
  }

  default int userdataPort() {
    return 8092;
  }
}
