package com.onehundredtwentyninth.rangiffler.test.gql.userdata;

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
import com.onehundredtwentyninth.rangiffler.model.gql.GqlFriendStatus;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlFriendshipAction;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlFriendshipInput;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
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

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[gql] Userdata")
class UpdateUserFriendshipTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[gql] Отправить заявку в друзья")
  @ApiLogin
  @CreateUser
  @Test
  void sentFriendshipRequestTest(@Token String token, TestUser user, @Extras TestUser[] users,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    final var input = new GqlFriendshipInput(users[0].getId(), GqlFriendshipAction.ADD);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(users[0].getId())
            .hasUsername(users[0].getUsername())
            .hasFriendStatus(GqlFriendStatus.INVITATION_SENT)
    );

    final var friendship = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getId(),
                users[0].getId()
            ),
            Optional::isPresent
        );

    Assertions.assertThat(friendship)
        .describedAs("Заявка в друзья есть в БД")
        .isPresent();

    Assertions.assertThat(friendship.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья")
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("[gql] Принять заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    var actionTarget = user.getIncomeInvitations().get(0);
    final var input = new GqlFriendshipInput(actionTarget.getId(), GqlFriendshipAction.ACCEPT);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(actionTarget.getId())
            .hasUsername(actionTarget.getUsername())
            .hasFriendStatus(GqlFriendStatus.FRIEND)
    );

    final var friendship = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getIncomeInvitations().get(0).getId(),
                user.getId()
            ),
            Optional::isPresent
        );

    Assertions.assertThat(friendship)
        .describedAs("Заявка в друзья есть в БД")
        .isPresent();

    Assertions.assertThat(friendship.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья")
        .isEqualTo(FriendshipStatus.ACCEPTED);
  }

  @DisplayName("[gql] Отклонить заявку в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectFriendshipRequestTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    var actionTarget = user.getIncomeInvitations().get(0);
    final var input = new GqlFriendshipInput(actionTarget.getId(), GqlFriendshipAction.REJECT);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(actionTarget.getId())
            .hasUsername(actionTarget.getUsername())
            .friendStatusIsNull()
    );

    Assertions.assertThatNoException()
        .describedAs("Заявка на дружбу отсутствует в БД")
        .isThrownBy(() ->
            Awaitility.await("Ожидаем удаления заявки на дружбу из БД")
                .atMost(Duration.ofMillis(10000))
                .pollInterval(Duration.ofMillis(1000))
                .ignoreExceptions()
                .until(() -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                        actionTarget.getId(),
                        user.getId()
                    ).isEmpty()
                )
        );
  }

  @DisplayName("[gql] Удаление из друзей")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend
      }
  )
  @Test
  void deleteFriendshipTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/friendshipAction.json") GqlRequest request) {
    var actionTarget = user.getFriends().get(0);
    final var input = new GqlFriendshipInput(actionTarget.getId(), GqlFriendshipAction.DELETE);
    request.variables().put("input", input);
    final var response = gatewayClient.friendshipAction(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(actionTarget.getId())
            .hasUsername(actionTarget.getUsername())
            .friendStatusIsNull()
    );

    Assertions.assertThatNoException()
        .describedAs("Дружба отсутствует в БД")
        .isThrownBy(() ->
            Awaitility.await("Ожидаем удаления дружбы из БД")
                .atMost(Duration.ofMillis(10000))
                .pollInterval(Duration.ofMillis(1000))
                .ignoreExceptions()
                .until(() -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                        user.getId(),
                        actionTarget.getId()
                    ).isEmpty()
                )
        );
  }
}
