package com.onehundredtwentyninth.rangiffler.test.web.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.PeoplePage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[web] Userdata")
class IncomeInvitationsTest extends BaseWebTest {

  @Inject
  private PeoplePage peoplePage;

  @DisplayName("[web] Получение всех входящих заявок в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void incomeInvitationsShouldBePresented(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .usersCountShouldBeEqualTo(2)
        .usersShouldBePresentedInTable(user.getIncomeInvitations().get(0), user.getIncomeInvitations().get(1));
  }

  @DisplayName("[web] Получение входящих заявок в друзья пользователя с фильтрацией по username")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void incomeInvitationsWithUsernameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .search(user.getIncomeInvitations().get(0).getUsername())
        .exactlyUsersShouldBePresentedInTable(user.getIncomeInvitations().get(0));
  }

  @DisplayName("[web] Получение входящих заявок в друзья пользователя с фильтрацией по firstname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void incomeInvitationsWithFirstnameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .search(user.getIncomeInvitations().get(0).getFirstname())
        .exactlyUsersShouldBePresentedInTable(user.getIncomeInvitations().get(0));
  }

  @DisplayName("[web] Получение входящих заявок в друзья пользователя с фильтрацией по lastname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void incomeInvitationsWithLastnameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .search(user.getIncomeInvitations().get(0).getLastName())
        .exactlyUsersShouldBePresentedInTable(user.getIncomeInvitations().get(0));
  }

  @DisplayName("[web] Отсутствие друзей и исходящих заявок в списке входящих заявок в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend,
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void outcomeInvitationAndFriendsFriendsShouldNotBePresented(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .search(user.getIncomeInvitations().get(0).getLastName())
        .exactlyUsersShouldBePresentedInTable(user.getIncomeInvitations().get(0));
  }
}
