package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import static com.onehundredtwentyninth.rangiffler.grpc.FriendStatus.INVITATION_RECEIVED;

import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[grpc] Userdata")
class GetUserFriendsIncomeRequestsTest extends GrpcUserdataTestBase {

  @DisplayName("[grpc] Получение всех входящих заявок в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getAllUserFriendsIncomeInvitationsTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(2)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getIncomeInvitations().get(0), INVITATION_RECEIVED))
            .containsUser(UserEntityMapper.toMessage(user.getIncomeInvitations().get(1), INVITATION_RECEIVED))
    );
  }

  @DisplayName("[grpc] Получение входящих заявок в друзья пользователя с фильтрацией по username")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithUsernameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getIncomeInvitations().get(0).getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getIncomeInvitations().get(0), INVITATION_RECEIVED))
            .notContainsUserWithName(user.getIncomeInvitations().get(1).getUsername())
    );
  }

  @DisplayName("[grpc] Получение входящих заявок в друзья пользователя с фильтрацией по firstname")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithFirstnameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getIncomeInvitations().get(0).getFirstname())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getIncomeInvitations().get(0), INVITATION_RECEIVED))
            .notContainsUserWithName(user.getIncomeInvitations().get(1).getUsername())
    );
  }

  @DisplayName("[grpc] Получение входящих заявок в друзья пользователя с фильтрацией по lastname")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithLastnameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getIncomeInvitations().get(0).getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getIncomeInvitations().get(0), INVITATION_RECEIVED))
            .notContainsUserWithName(user.getIncomeInvitations().get(1).getUsername())
    );
  }

  @DisplayName("[grpc] Отсутствие друзей и исходящих заявок в списке входящих заявок в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend,
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithoutPendingTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getIncomeInvitations().get(0), INVITATION_RECEIVED))
            .notContainsUserWithName(user.getOutcomeInvitations().get(0).getUsername())
            .notContainsUserWithName(user.getFriends().get(0).getUsername())
    );
  }
}
