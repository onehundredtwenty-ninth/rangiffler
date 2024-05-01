package com.onehundredtwentyninth.rangiffler.test.web;

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
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyProfilePage;
import com.onehundredtwentyninth.rangiffler.utils.ImageUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
class UpdateUserTest extends BaseWebTest {

  @Inject
  private MyProfilePage myProfilePage;
  @Inject
  private UserRepository userRepository;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private Faker faker;

  @DisplayName("Обновление пользователя")
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

    final var dbUser = userRepository.findById(user.getId());
    final var country = countryRepository.findCountryByCode(CountryCodes.CN.getCode());

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
}
