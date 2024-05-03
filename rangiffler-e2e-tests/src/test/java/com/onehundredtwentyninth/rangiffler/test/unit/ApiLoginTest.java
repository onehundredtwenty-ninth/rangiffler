package com.onehundredtwentyninth.rangiffler.test.unit;

import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.ApiLoginExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.CreateUserExtension;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({CreateUserExtension.class, ApiLoginExtension.class})
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

  @ApiLogin(username = "bee", password = "123")
  @Test
  void apiLoginByAnnotationTest(@Token String token) {
    Assertions.assertNotNull(token);
  }

  @ApiLogin
  @CreateUser(username = "bee48", password = "123")
  @Test
  void apiLoginForCreatedUserTest(@Token String token, TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(token),
        () -> Assertions.assertEquals("bee48", user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cc91f80-f195-11ee-9b32-0242ac110002", user.getCountry().getId().toString())
    );
  }

  @ApiLogin
  @CreateUser
  @Test
  void apiLoginForRandomCreatedUserTest(@Token String token, TestUser user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(token),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cc91f80-f195-11ee-9b32-0242ac110002", user.getCountry().getId().toString())
    );
  }
}
