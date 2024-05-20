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
class FriendsListTest extends BaseWebTest {

  @Inject
  private PeoplePage peoplePage;

  @DisplayName("[web] Отображение списка друзей")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void friendsShouldBePresented(TestUser user) {
    peoplePage.open()
        .openFriendsTab()
        .usersCountShouldBeEqualTo(2)
        .usersShouldBePresentedInTable(user.getFriends().get(0), user.getFriends().get(1));
  }

  @DisplayName("[web] Получение друзей пользователя с фильтрацией по username")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void friendsWithUsernameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openFriendsTab()
        .search(user.getFriends().get(0).getUsername())
        .exactlyUsersShouldBePresentedInTable(user.getFriends().get(0));
  }

  @DisplayName("[web] Получение друзей пользователя с фильтрацией по firstname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void friendsWithFirstnameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openFriendsTab()
        .search(user.getFriends().get(0).getFirstname())
        .exactlyUsersShouldBePresentedInTable(user.getFriends().get(0));
  }

  @DisplayName("[web] Получение друзей пользователя с фильтрацией по lastname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void friendsWithLastnameFilterShouldBePresented(TestUser user) {
    peoplePage.open()
        .openFriendsTab()
        .search(user.getFriends().get(0).getLastName())
        .exactlyUsersShouldBePresentedInTable(user.getFriends().get(0));
  }

  @DisplayName("[web] Отсутствие неподтверженных друзей в списке друзей пользователя")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend,
          @Friend(pending = true),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void pendingFriendsShouldNotBePresented(TestUser user) {
    peoplePage.open()
        .openFriendsTab()
        .search(user.getFriends().get(0).getLastName())
        .exactlyUsersShouldBePresentedInTable(user.getFriends().get(0));
  }
}
