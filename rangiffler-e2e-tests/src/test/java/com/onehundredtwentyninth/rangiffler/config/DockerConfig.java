package com.onehundredtwentyninth.rangiffler.config;

import com.codeborne.selenide.Configuration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.chrome.ChromeOptions;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DockerConfig implements Config {

  static final DockerConfig INSTANCE = new DockerConfig();

  static {
    Configuration.baseUrl = "http://frontend.rangiffler.dc";
    Configuration.remote = "http://selenoid:4444/wd/hub";
    Configuration.browser = "chrome";
    Configuration.browserVersion = "117.0";
    Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
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
    return "geo.rangiffler.dc";
  }

  @Override
  public String frontUrl() {
    return "http://frontend.rangiffler.dc";
  }

  @Override
  public String photoHost() {
    return "photo.rangiffler.dc";
  }

  @Override
  public String userdataHost() {
    return "userdata.rangiffler.dc";
  }

  @Override
  public String jdbcHost() {
    return "rangiffler-all-db";
  }
}
