package com.onehundredtwentyninth.rangiffler.test.gql;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.model.FriendshipInput;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class UpdateUserFriendshipTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Отправить заявку в друзья")
  @ApiLogin
  @CreateUser
  @Test
  void sentFriendshipRequestTest(@Token String token, TestUser user, @Extras TestUser[] users,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(users[0].getId(), FriendshipAction.ADD);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(user.getId())
            .hasUsername(user.getUsername())
    );

    final var friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getId(),
        users[0].getId()
    );

    Assertions.assertThat(friendship)
        .describedAs("Заявка в друзья есть в БД")
        .isPresent();

    Assertions.assertThat(friendship.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья")
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("Принять заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getFriends().get(0).getId(), FriendshipAction.ACCEPT);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(user.getId())
            .hasUsername(user.getUsername())
    );

    final var friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getFriends().get(0).getId(),
        user.getId()
    );

    Assertions.assertThat(friendship)
        .describedAs("Заявка в друзья есть в БД")
        .isPresent();

    Assertions.assertThat(friendship.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья")
        .isEqualTo(FriendshipStatus.ACCEPTED);
  }

  @DisplayName("Отклонить заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getFriends().get(0).getId(), FriendshipAction.REJECT);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(user.getId())
            .hasUsername(user.getUsername())
    );

    final var friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getFriends().get(0).getId(),
        user.getId()
    );

    Assertions.assertThat(friendship)
        .describedAs("Заявка на дружбу отсутствует в БД")
        .isEmpty();
  }

  @DisplayName("Удаление из друзей")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend
      }
  )
  @Test
  void deleteFriendshipTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getFriends().get(0).getId(), FriendshipAction.DELETE);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(user.getId())
            .hasUsername(user.getUsername())
    );

    final var friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getId(),
        user.getFriends().get(0).getId()
    );

    Assertions.assertThat(friendship)
        .describedAs("Запись о дружбе отсутствует в БД")
        .isEmpty();
  }
}
