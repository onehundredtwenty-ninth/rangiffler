package com.onehundredtwentyninth.rangiffler.test.web;

import com.github.javafaker.Faker;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.page.RegisterPage;
import com.onehundredtwentyninth.rangiffler.page.StartPage;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.REGISTER)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.REGISTER)})
class RegistrationTest extends BaseWebTest {

  @Inject
  private UserService userService;
  @Inject
  private StartPage startPage;
  @Inject
  private RegisterPage registerPage;
  @Inject
  private Faker faker;
  private String username;

  @DisplayName("Регистрация")
  @Test
  void registerTest() {
    startPage
        .open()
        .clickRegisterBtn();
    username = faker.name().username();
    registerPage.fillRegisterPage(username, faker.internet().password(3, 12));
    registerPage.successSubmit();
  }

  @AfterEach
  void after() {
    userService.deleteUser(username);
  }
}
