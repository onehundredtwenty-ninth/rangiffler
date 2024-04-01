package com.onehundredtwentyninth.rangiffler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RangifflerGatewayServiceConfig {

  public static final int TEN_MB = 10 * 1024 * 1024;
  public static final int ONE_MB = 1024 * 1024;

  private final String rangifflerUserdataBaseUri;

  @Autowired
  public RangifflerGatewayServiceConfig(@Value("${rangiffler-userdata.base-uri}") String rangifflerUserdataBaseUri) {
    this.rangifflerUserdataBaseUri = rangifflerUserdataBaseUri;
  }

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(TEN_MB))
            .build())
        .build();
  }
}
