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
class OutcomeInvitationsTest extends BaseWebTest {

  @Inject
  private PeoplePage peoplePage;

  @DisplayName("[web] Получение всех исходящих заявок в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void outcomeInvitationsShouldBePresented(TestUser user) {
    peoplePage.open()
        .openOutcomeInvitationsTab()
        .exactlyUsersShouldBePresentedInTable(user.getOutcomeInvitations().get(0), user.getOutcomeInvitations().get(1));
  }

  @DisplayName("[web] Получение исходящих заявок в друзья пользователя с фильтрацией по username")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void outcomeInvitationsWithUsernameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openOutcomeInvitationsTab()
        .search(user.getOutcomeInvitations().get(0).getUsername())
        .exactlyUsersShouldBePresentedInTable(user.getOutcomeInvitations().get(0));
  }

  @DisplayName("[web] Получение исходящих заявок в друзья пользователя с фильтрацией по firstname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void outcomeInvitationsWithFirstnameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openOutcomeInvitationsTab()
        .search(user.getOutcomeInvitations().get(0).getFirstname())
        .exactlyUsersShouldBePresentedInTable(user.getOutcomeInvitations().get(0));
  }

  @DisplayName("[web] Получение исходящих заявок в друзья пользователя с фильтрацией по lastname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void outcomeInvitationsWithLastnameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openOutcomeInvitationsTab()
        .search(user.getOutcomeInvitations().get(0).getLastName())
        .exactlyUsersShouldBePresentedInTable(user.getOutcomeInvitations().get(0));
  }

  @DisplayName("[web] Отсутствие друзей и исходящих заявок в списке исходящих заявок в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend,
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME)
      }
  )
  @Test
  void incomeInvitationAndFriendsFriendsShouldNotBePresented(TestUser user) {
    peoplePage.open()
        .openOutcomeInvitationsTab()
        .search(user.getOutcomeInvitations().get(0).getLastName())
        .exactlyUsersShouldBePresentedInTable(user.getOutcomeInvitations().get(0));
  }
}
