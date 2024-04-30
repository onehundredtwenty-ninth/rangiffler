package com.onehundredtwentyninth.rangiffler.test.web;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.PeoplePage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class PeopleListTest extends BaseWebTest {

  @Inject
  private PeoplePage peoplePage;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Получение всех пользователей по переданному username")
  @ApiLogin
  @CreateUser
  @Test
  void allPeopleWithUsernameShouldBePresented(@Extras TestUser[] users) {
    peoplePage.open()
        .openAllPeopleTab()
        .search(users[0].getUsername())
        .exactlyUsersShouldBePresentedInTable(users[0]);
  }

  @DisplayName("Получение пользователей при передаче SearchQuery username автора запроса")
  @ApiLogin
  @CreateUser
  @Test
  void currentUserShouldNotBePresentedInAllPeopleTab(TestUser user) {
    peoplePage.open()
        .openAllPeopleTab()
        .search(user.getUsername())
        .noUserYetMessageShouldBePresented();
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Получение всех пользователей по переданному firstname")
  @ApiLogin
  @CreateUser
  @Test
  void allPeopleWithFirstnameShouldBePresented(@Extras TestUser[] users) {
    peoplePage.open()
        .openAllPeopleTab()
        .search(users[0].getFirstname())
        .exactlyUsersShouldBePresentedInTable(users[0]);
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Получение всех пользователей по переданному lastname")
  @ApiLogin
  @CreateUser
  @Test
  void allPeopleWithLastnameShouldBePresented(@Extras TestUser[] users) {
    peoplePage.open()
        .openAllPeopleTab()
        .search(users[0].getLastName())
        .exactlyUsersShouldBePresentedInTable(users[0]);
  }
}
