package com.onehundredtwentyninth.rangiffler.test.web.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.PeoplePage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[web] Userdata")
class UpdateUserFriendshipTest extends BaseWebTest {

  @Inject
  private FriendshipRepository friendshipRepository;
  @Inject
  private PeoplePage peoplePage;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[web] Отправить заявку в друзья")
  @ApiLogin
  @CreateUser
  @Test
  void sentFriendshipRequestTest(TestUser user, @Extras TestUser[] users) {
    peoplePage.open()
        .openAllPeopleTab()
        .search(users[0].getUsername())
        .addFriend(users[0].getUsername());

    final var friendship = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getId(),
                users[0].getId()
            ),
            Optional::isPresent
        );

    Assertions.assertThat(friendship.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья")
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("[web] Принять заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptFriendshipRequestTest(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .acceptInvitation(user.getIncomeInvitations().get(0).getUsername());

    final var friendship = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getIncomeInvitations().get(0).getId(),
                user.getId()
            ),
            Optional::isPresent
        );

    Assertions.assertThat(friendship.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья")
        .isEqualTo(FriendshipStatus.ACCEPTED);
  }

  @DisplayName("[web] Отклонить заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectFriendshipRequestTest(TestUser user) {
    peoplePage.open()
        .openIncomeInvitationsTab()
        .rejectInvitation(user.getIncomeInvitations().get(0).getUsername());

    Awaitility.await("Ожидаем удаления заявки на дружбу из БД")
        .atMost(Duration.ofMillis(10000))
        .pollInterval(Duration.ofMillis(1000))
        .until(() -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getIncomeInvitations().get(0).getId(),
                user.getId()
            ).isEmpty()
        );
  }

  @DisplayName("[web] Удаление из друзей")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend
      }
  )
  @Test
  void deleteFriendshipTest(TestUser user) {
    peoplePage.open()
        .openFriendsTab()
        .deleteFriend(user.getFriends().get(0).getUsername());

    Awaitility.await("Ожидаем удаления дружбы из БД")
        .atMost(Duration.ofMillis(10000))
        .pollInterval(Duration.ofMillis(1000))
        .until(() -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getId(),
                user.getFriends().get(0).getId()
            ).isEmpty()
        );
  }
}
