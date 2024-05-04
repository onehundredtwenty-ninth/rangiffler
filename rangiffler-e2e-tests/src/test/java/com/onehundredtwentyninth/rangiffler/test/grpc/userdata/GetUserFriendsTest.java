package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import static com.onehundredtwentyninth.rangiffler.grpc.FriendStatus.FRIEND;

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
class GetUserFriendsTest extends GrpcUserdataTestBase {

  @DisplayName("[grpc] Получение всех друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getAllUserFriendsTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(2)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0), FRIEND))
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(1), FRIEND))
    );
  }

  @DisplayName("[grpc] Получение друзей пользователя с фильтрацией по username")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getUserFriendsWithUsernameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0), FRIEND))
            .notContainsUserWithName(user.getFriends().get(1).getUsername())
    );
  }

  @DisplayName("[grpc] Получение друзей пользователя с фильтрацией по firstname")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getUserFriendsWithFirstnameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getFirstname())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0), FRIEND))
            .notContainsUserWithName(user.getFriends().get(1).getUsername())
    );
  }

  @DisplayName("[grpc] Получение друзей пользователя с фильтрацией по lastname")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getUserFriendsWithLastnameFilterTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(UserEntityMapper.toMessage(user.getFriends().get(0), FRIEND))
            .notContainsUserWithName(user.getFriends().get(1).getUsername())
    );
  }

  @DisplayName("[grpc] Отсутствие неподтверженных друзей в списке друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend(pending = true),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsWithoutPendingTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getFriends().get(0).getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .notContainsUserWithName(user.getIncomeInvitations().get(0).getUsername())
            .notContainsUserWithName(user.getOutcomeInvitations().get(0).getUsername())
    );
  }
}
