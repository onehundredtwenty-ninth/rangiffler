package com.onehundredtwentyninth.rangiffler.test.gql.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[gql] Userdata")
class GetUserIncomeInvitationsTest {

  @Inject
  private GatewayClient gatewayClient;

  @DisplayName("[gql] Получение всех входящих заявок в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getIncomeInvitationsTest(@Token String token, TestUser user, @GqlRequestFile("gql/getInvitations.json") GqlRequest request) {
    var response = gatewayClient.getUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations())
            .hasEdgesCount(user.getIncomeInvitations().size())
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedFriend = user.getIncomeInvitations().get(0);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations().getEdges().get(0))
            .hasId(expectedFriend.getId())
            .hasUsername(expectedFriend.getUsername())
            .hasFirstName(expectedFriend.getFirstname())
            .hasLastName(expectedFriend.getLastName())
            .hasAvatar(expectedFriend.getAvatar())
    );
  }

  @DisplayName("[gql] Получение входящих заявок в друзья пользователя с фильтрацией по username")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getIncomeInvitationsWithUsernameFilterTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/getInvitations.json") GqlRequest request) {
    request.variables().put("searchQuery", user.getIncomeInvitations().get(0).getUsername());
    var response = gatewayClient.getUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedFriend = user.getIncomeInvitations().get(0);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations().getEdges().get(0))
            .hasId(expectedFriend.getId())
            .hasUsername(expectedFriend.getUsername())
            .hasFirstName(expectedFriend.getFirstname())
            .hasLastName(expectedFriend.getLastName())
            .hasAvatar(expectedFriend.getAvatar())
    );
  }

  @DisplayName("[gql] Получение входящих заявок в друзья пользователя с фильтрацией по firstname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getIncomeInvitationsWithFirstnameFilterTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/getInvitations.json") GqlRequest request) {
    request.variables().put("searchQuery", user.getIncomeInvitations().get(1).getFirstname());
    var response = gatewayClient.getUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedFriend = user.getIncomeInvitations().get(1);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations().getEdges().get(0))
            .hasId(expectedFriend.getId())
            .hasUsername(expectedFriend.getUsername())
            .hasFirstName(expectedFriend.getFirstname())
            .hasLastName(expectedFriend.getLastName())
            .hasAvatar(expectedFriend.getAvatar())
    );
  }

  @DisplayName("[gql] Получение входящих заявок в друзья пользователя с фильтрацией по lastname")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getIncomeInvitationsWithSurnameFilterTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/getInvitations.json") GqlRequest request) {
    request.variables().put("searchQuery", user.getIncomeInvitations().get(1).getLastName());
    var response = gatewayClient.getUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedFriend = user.getIncomeInvitations().get(1);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations().getEdges().get(0))
            .hasId(expectedFriend.getId())
            .hasUsername(expectedFriend.getUsername())
            .hasFirstName(expectedFriend.getFirstname())
            .hasLastName(expectedFriend.getLastName())
            .hasAvatar(expectedFriend.getAvatar())
    );
  }

  @DisplayName("[gql] Отсутствие друзей и исходящих заявок в списке входящих заявок в друзья")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend,
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getIncomeInvitationsWithoutPendingTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/getInvitations.json") GqlRequest request) {
    var response = gatewayClient.getUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedFriend = user.getIncomeInvitations().get(0);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getIncomeInvitations().getEdges().get(0))
            .hasId(expectedFriend.getId())
            .hasUsername(expectedFriend.getUsername())
            .hasFirstName(expectedFriend.getFirstname())
            .hasLastName(expectedFriend.getLastName())
            .hasAvatar(expectedFriend.getAvatar())
    );
  }
}
