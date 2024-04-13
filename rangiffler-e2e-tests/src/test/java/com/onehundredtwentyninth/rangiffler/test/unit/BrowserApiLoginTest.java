package com.onehundredtwentyninth.rangiffler.test.unit;

import com.onehundredtwentyninth.rangiffler.jupiter.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.ApiLoginExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.BrowserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.Token;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({CreateUserExtension.class, ApiLoginExtension.class, BrowserExtension.class})
class BrowserApiLoginTest {

  private final MyTravelsPage myTravelsPage = new MyTravelsPage();

  @ApiLogin(username = "bee", password = "123")
  @Test
  void apiLoginForRandomCreatedUserTest(@Token String token) {
    Assertions.assertNotNull(token);
    myTravelsPage.travelsMapHeaderShouldBeVisible();
  }
}
