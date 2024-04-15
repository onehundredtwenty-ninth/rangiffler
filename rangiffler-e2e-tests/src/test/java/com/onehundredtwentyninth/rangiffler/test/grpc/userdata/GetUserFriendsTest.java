package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class GetUserFriendsTest extends GrpcUserdataTestBase {

  @DisplayName("Получение всех друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getAllUserFriendsTest(User user, User[] friends) {
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
            .containsUser(friends[0])
            .containsUser(friends[1])
    );
  }

  @DisplayName("Получение друзей пользователя с фильтрацией по username")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getUserFriendsWithUsernameFilterTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(friends[0])
            .notContainsUser(friends[1])
    );
  }

  @DisplayName("Получение друзей пользователя с фильтрацией по firstname")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getUserFriendsWithFirstnameFilterTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getFirstname())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(friends[0])
            .notContainsUser(friends[1])
    );
  }

  @DisplayName("Получение друзей пользователя с фильтрацией по lastname")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getUserFriendsWithLastnameFilterTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(friends[0])
            .notContainsUser(friends[1])
    );
  }

  @DisplayName("Отсутствие неподтверженных друзей в списке друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend(pending = true),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsWithoutPendingTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getUserFriends(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .notContainsUser(friends[1])
            .notContainsUser(friends[2])
    );
  }
}
