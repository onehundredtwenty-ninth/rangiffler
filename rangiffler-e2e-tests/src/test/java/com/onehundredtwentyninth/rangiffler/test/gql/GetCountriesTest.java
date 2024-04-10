package com.onehundredtwentyninth.rangiffler.test.gql;

import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.jupiter.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GetCountriesTest {

  private final GatewayClient gatewayClient = new GatewayClient();

  @Test
  void getCountriesTest(@GqlRequestFile("gql/getCountries.json") GqlRequest request) {
    var token = new AuthService().doLogin("bee", "123").idToken();
    var countriesResponse = gatewayClient.getCountries(token, request);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(countriesResponse),
        () -> Assertions.assertNotNull(countriesResponse.getData().getCountries()),
        () -> Assertions.assertEquals(238, countriesResponse.getData().getCountries().size()),
        () -> Assertions.assertNotNull(countriesResponse.getData().getCountries().get(0).code()),
        () -> Assertions.assertNotNull(countriesResponse.getData().getCountries().get(0).name()),
        () -> Assertions.assertNotNull(countriesResponse.getData().getCountries().get(0).flag())
    );
  }
}
