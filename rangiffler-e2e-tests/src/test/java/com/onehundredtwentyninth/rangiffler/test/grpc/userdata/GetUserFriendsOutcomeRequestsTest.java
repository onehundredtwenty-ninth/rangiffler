package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

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
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class GetUserFriendsOutcomeRequestsTest extends GrpcUserdataTestBase {

  @DisplayName("Получение всех исходящих заявок в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getAllUserFriendsOutcomeInvitationsTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipAddresses(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(2)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0)))
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(1)))
    );
  }

  @DisplayName("Получение исходящих заявок в друзья пользователя с фильтрацией по username")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsOutcomeInvitationsWithUsernameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipAddresses(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0)))
            .notContainsUser(UserEntityMapper.toMessage(user.getFriends().get(1)))
    );
  }

  @DisplayName("Получение исходящих заявок в друзья пользователя с фильтрацией по firstname")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsOutcomeInvitationsWithFirstnameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getFirstname())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipAddresses(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0)))
            .notContainsUser(UserEntityMapper.toMessage(user.getFriends().get(1)))
    );
  }

  @DisplayName("Получение исходящих заявок в друзья пользователя с фильтрацией по lastname")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsOutcomeInvitationsWithLastnameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipAddresses(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0)))
            .notContainsUser(UserEntityMapper.toMessage(user.getFriends().get(1)))
    );
  }

  @DisplayName("Отсутствие друзей и входящих заявок в списке исходящих заявок в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME),
          @Friend,
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME)
      }
  )
  @Test
  void getUserFriendsOutcomeInvitationsWithoutPendingTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipAddresses(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .notContainsUser(UserEntityMapper.toMessage(user.getFriends().get(1)))
            .notContainsUser(UserEntityMapper.toMessage(user.getFriends().get(2)))
    );
  }
}
