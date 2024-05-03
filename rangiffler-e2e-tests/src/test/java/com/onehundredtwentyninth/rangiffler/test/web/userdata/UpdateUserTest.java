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
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyProfilePage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import com.onehundredtwentyninth.rangiffler.utils.ImageUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
@DisplayName("[web] Userdata")
class UpdateUserTest extends BaseWebTest {

  @Inject
  private MyProfilePage myProfilePage;
  @Inject
  private UserRepository userRepository;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private Faker faker;

  @DisplayName("[web] Обновление пользователя")
  @ApiLogin
  @CreateUser
  @Test
  void updateUserTest(TestUser user) {
    var newUserData = TestUser.builder()
        .firstname(faker.name().firstName())
        .lastName(faker.name().lastName())
        .build();

    myProfilePage.open()
        .pageHeaderShouldBeVisible()
        .setFirstname(newUserData.getFirstname())
        .setLastname(newUserData.getLastName())
        .setLocation(CountryCodes.CN.getCode())
        .setAvatar("image/defaultAvatar.png")
        .saveChanges();

    final var country = countryRepository.findRequiredCountryByCode(CountryCodes.CN.getCode());
    final var dbUser = Awaitility.await("Ожидаем обновления пользователя в БД")
        .atMost(Duration.ofMillis(10000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> userRepository.findRequiredById(user.getId()),
            userEntity -> newUserData.getFirstname().equals(userEntity.getFirstname())
        );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(dbUser)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(newUserData.getFirstname())
            .hasLastName(newUserData.getLastName())
            .hasAvatar(ImageUtils.getImageFromResourceAsBase64("defaultAvatar.png").getBytes(StandardCharsets.UTF_8))
            .hasCountryId(country.getId())
    );
  }

  @DisplayName("[web] Отмена обновления данных пользователя")
  @ApiLogin
  @CreateUser
  @Test
  void resetUpdateUserTest(TestUser user) {
    myProfilePage.open()
        .pageHeaderShouldBeVisible()
        .setFirstname(faker.name().firstName())
        .setLastname(faker.name().lastName())
        .setLocation(CountryCodes.CN.getCode())
        .setAvatar("image/defaultAvatar.png")
        .resetChanges();

    myProfilePage
        .firstnameShouldBe(user.getFirstname())
        .lastnameShouldBe(user.getLastName())
        .locationNameShouldBe(user.getCountry().getName())
        .locationFlagShouldBe(user.getCountry().getFlag())
        .avatarShouldBe(user.getAvatar());

    final var dbUser = userRepository.findRequiredById(user.getId());

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(dbUser)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar())
            .hasCountryId(user.getCountry().getId())
    );
  }
}
