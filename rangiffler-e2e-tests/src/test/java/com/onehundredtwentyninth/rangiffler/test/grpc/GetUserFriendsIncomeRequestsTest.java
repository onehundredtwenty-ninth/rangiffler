package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GrpcTest
@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.USERS), @Tag(Features.USER)})
class GetUserFriendsIncomeRequestsTest {

  private static final Config CFG = Config.getInstance();
  private RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.userdataHost(), CFG.userdataPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerUserdataServiceGrpc.newBlockingStub(channel);
  }

  @DisplayName("Получение всех входящих заявок в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getAllUserFriendsIncomeInvitationsTest(User user, User[] friends) {
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
            .containsUser(friends[0])
            .containsUser(friends[1])
    );
  }

  @DisplayName("Получение входящих заявок в друзья пользователя с фильтрацией по username")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithUsernameFilterTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(friends[0])
            .notContainsUser(friends[1])
    );
  }

  @DisplayName("Получение входящих заявок в друзья пользователя с фильтрацией по firstname")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithFirstnameFilterTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getFirstname())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(friends[0])
            .notContainsUser(friends[1])
    );
  }

  @DisplayName("Получение входящих заявок в друзья пользователя с фильтрацией по lastname")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend(pending = true)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithLastnameFilterTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .containsUser(friends[0])
            .notContainsUser(friends[1])
    );
  }

  @DisplayName("Отсутствие друзей и исходящих заявок в списке входящих заявок в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true),
          @Friend,
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getUserFriendsIncomeInvitationsWithoutPendingTest(User user, User[] friends) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(friends[0].getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getFriendshipRequests(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .notContainsUser(friends[1])
            .notContainsUser(friends[2])
    );
  }
}
