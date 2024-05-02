package com.onehundredtwentyninth.rangiffler.test.gql.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.model.FriendshipInput;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[gql] Userdata")
class IllegalUpdateUserFriendshipTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[gql] Удалить несуществующую дружбу")
  @ApiLogin
  @CreateUser
  @Test
  void deleteNonExistentFriendshipRequestTest(@Token String token, TestUser user, @Extras TestUser[] users,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(users[0].getId(), FriendshipAction.DELETE);
    request.variables().put("input", input);

    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasFriendshipNotFoundMessage(user.getUsername(), users[0].getUsername())
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Принять собственную заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void acceptNonExistentFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getOutcomeInvitations().get(0).getId(), FriendshipAction.ACCEPT);
    request.variables().put("input", input);

    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasFriendshipRequestNotFoundMessage(user.getOutcomeInvitations().get(0).getUsername(), user.getUsername())
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Отклонить собственную заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void rejectNonExistentFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getOutcomeInvitations().get(0).getId(), FriendshipAction.ACCEPT);
    request.variables().put("input", input);

    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasFriendshipRequestNotFoundMessage(user.getOutcomeInvitations().get(0).getUsername(), user.getUsername())
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Отправка FriendshipAction UNSPECIFIED")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME)
      }
  )
  @Test
  void unspecifiedFriendshipActionTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    request.variables().put(
        "input", Map.of(
            "user", user.getIncomeInvitations().get(0).getId(),
            "action", "UNSPECIFIED"
        )
    );
    final var response = gatewayClient.friendshipAction(token, request);

    var expectedMessage = "Variable 'input' has an invalid value: Invalid input for enum 'FriendshipAction'. No value found for name 'UNSPECIFIED'";
    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasMessage(expectedMessage)
              .hasExtensions(Map.of("classification", "ValidationError"));
        }
    );
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[gql] Отправить повторно заявку в друзья")
  @ApiLogin
  @CreateUser
  @Test
  void sentSecondFriendshipRequestTest(@Token String token, TestUser user, @Extras TestUser[] users,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(users[0].getId(), FriendshipAction.ADD);
    request.variables().put("input", input);

    gatewayClient.friendshipAction(token, request);
    final var response = gatewayClient.friendshipAction(token, request);

    var expectedMessage = "ERROR: duplicate key value violates unique constraint \"friendship_requester_id_addressee_id_key\"";
    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .messageContains(expectedMessage)
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Принять заявку в друзья повторно")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptTwiceFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getIncomeInvitations().get(0).getId(), FriendshipAction.ACCEPT);
    request.variables().put("input", input);

    gatewayClient.friendshipAction(token, request);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasFriendshipRequestNotFoundMessage(user.getIncomeInvitations().get(0).getUsername(), user.getUsername())
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Отклонить ранее принятую заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectTwiceFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(user.getIncomeInvitations().get(0).getId(), FriendshipAction.ACCEPT);
    request.variables().put("input", input);

    gatewayClient.friendshipAction(token, request);

    final var rejectInput = new FriendshipInput(user.getIncomeInvitations().get(0).getId(), FriendshipAction.REJECT);
    request.variables().put("input", rejectInput);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasFriendshipRequestNotFoundMessage(user.getIncomeInvitations().get(0).getUsername(), user.getUsername())
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Обновить заявку в друзья с несуществующим пользователем")
  @ApiLogin
  @CreateUser
  @Test
  void sentFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new FriendshipInput(UUID.fromString("00000000-0000-0000-0000-000000000000"),
        FriendshipAction.ADD);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasUserNotFoundMessage(UUID.fromString("00000000-0000-0000-0000-000000000000"))
              .hasPath(List.of("friendship"))
              .hasInternalErrorExtension();
        }
    );
  }
}
