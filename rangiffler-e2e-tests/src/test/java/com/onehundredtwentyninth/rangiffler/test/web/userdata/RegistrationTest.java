package com.onehundredtwentyninth.rangiffler.test.web.userdata;

import com.github.javafaker.Faker;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.page.RegisterPage;
import com.onehundredtwentyninth.rangiffler.page.StartPage;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.Objects;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.REGISTER)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.REGISTER)})
@DisplayName("[web] Userdata")
class RegistrationTest extends BaseWebTest {

  @Inject
  private UserService userService;
  @Inject
  private UserRepository userRepository;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private StartPage startPage;
  @Inject
  private RegisterPage registerPage;
  @Inject
  private Faker faker;
  private String username;

  @DisplayName("[web] Регистрация")
  @Test
  void registerTest() {
    username = faker.name().username();
    final var defaultCountry = countryRepository.findRequiredCountryByCode(CountryCodes.DE.getCode());

    startPage
        .open()
        .clickRegisterBtn();

    registerPage.fillRegisterPage(username, faker.internet().password(3, 12));
    registerPage.successSubmit();

    final var userInUserdata = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .until(
            () -> userRepository.findRequiredByUsername(username),
            Objects::nonNull
        );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(userInUserdata)
            .hasUsername(username)
            .hasFirstName(username)
            .hasLastName(username)
            .hasCountryId(defaultCountry.getId())
    );
  }

  @AfterEach
  void after() {
    userService.deleteUser(username);
  }
}
