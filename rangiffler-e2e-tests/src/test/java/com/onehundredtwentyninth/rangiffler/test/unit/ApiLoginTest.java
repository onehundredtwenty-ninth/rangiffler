package com.onehundredtwentyninth.rangiffler.test.unit;

import com.onehundredtwentyninth.rangiffler.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiLoginTest {

  private final AuthService authService = new AuthService();

  @Test
  void apiLoginTest() {
    var tokenResponse = authService.doLogin("bee", "123");
    Assertions.assertAll(
        () -> Assertions.assertNotNull(tokenResponse.accessToken()),
        () -> Assertions.assertNotNull(tokenResponse.refreshToken()),
        () -> Assertions.assertEquals("openid", tokenResponse.scope()),
        () -> Assertions.assertNotNull(tokenResponse.idToken()),
        () -> Assertions.assertEquals("Bearer", tokenResponse.tokenType()),
        () -> Assertions.assertNotNull(tokenResponse.expiresIn())
    );
  }
}
