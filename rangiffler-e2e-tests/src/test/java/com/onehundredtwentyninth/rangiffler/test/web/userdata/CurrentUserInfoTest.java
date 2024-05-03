package com.onehundredtwentyninth.rangiffler.test.web.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyProfilePage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
@DisplayName("[web] Userdata")
class CurrentUserInfoTest extends BaseWebTest {

  @Inject
  private MyProfilePage myProfilePage;

  @DisplayName("[web] Отображение данных пользователя на странице профайла")
  @ApiLogin
  @CreateUser
  @Test
  void currentUserInfoTest(TestUser user) {
    myProfilePage.open()
        .pageHeaderShouldBeVisible()
        .usernameShouldBe(user.getUsername())
        .firstnameShouldBe(user.getFirstname())
        .lastnameShouldBe(user.getLastName())
        .locationNameShouldBe(user.getCountry().getName())
        .locationFlagShouldBe(user.getCountry().getFlag())
        .avatarShouldBe(user.getAvatar());
  }
}
