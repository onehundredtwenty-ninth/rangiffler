package com.onehundredtwentyninth.rangiffler.test.web.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.LoginPage;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.page.StartPage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.LOGIN)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.LOGIN)})
@DisplayName("[web] Userdata")
class LoginTest extends BaseWebTest {

  @Inject
  private StartPage startPage;
  @Inject
  private LoginPage loginPage;
  @Inject
  private MyTravelsPage myTravelsPage;

  @DisplayName("[web] Авторизация")
  @CreateUser
  @Test
  void loginTest(TestUser user) {
    startPage
        .open()
        .clickLoginBtn();
    loginPage.login(user.getUsername(), user.getTestData().password());
    myTravelsPage.travelsMapHeaderShouldBeVisible();
  }
}
