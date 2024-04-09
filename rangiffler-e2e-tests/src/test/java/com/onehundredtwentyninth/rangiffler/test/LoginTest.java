package com.onehundredtwentyninth.rangiffler.test;

import com.onehundredtwentyninth.rangiffler.page.LoginPage;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.page.StartPage;
import org.junit.jupiter.api.Test;

class LoginTest {

  private final StartPage startPage = new StartPage();
  private final LoginPage loginPage = new LoginPage();
  private final MyTravelsPage myTravelsPage = new MyTravelsPage();

  @Test
  void loginTest() {
    startPage
        .open()
        .clickLoginBtn();
    loginPage.login("bee", "123");
    myTravelsPage.travelsMapHeaderShouldBeVisible();
  }
}
