package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcStatusExceptionAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.grpc.UpdateUserFriendshipRequest;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.Friends;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class IllegalUpdateUserFriendshipTest extends GrpcUserdataTestBase {

  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Удалить несуществующую заявку в друзья")
  @CreateUser
  @Test
  void deleteNonExistentFriendshipRequestTest(User user, @Extras User[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(users[0].getId())
        .setAction(FriendshipAction.DELETE)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipNotFoundMessage(user.getUsername(), users[0].getUsername());
  }

  @DisplayName("Принять собственную заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void acceptNonExistentFriendshipRequestTest(User user, @Friends User[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(users[0].getId())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(users[0].getUsername(), user.getUsername());
  }

  @DisplayName("Отклонить собственную заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void rejectNonExistentFriendshipRequestTest(User user, @Friends User[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(users[0].getId())
        .setAction(FriendshipAction.REJECT)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(users[0].getUsername(), user.getUsername());
  }

  @DisplayName("Отправка FriendshipAction UNSPECIFIED")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void unspecifiedFriendshipActionTest(User user, @Friends User[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(users[0].getId())
        .setAction(FriendshipAction.UNSPECIFIED)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasAbortedMessage();
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Отправить повторно заявку в друзья")
  @CreateUser
  @Test
  void sentSecondFriendshipRequestTest(User user, @Extras User[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(users[0].getId())
        .setAction(FriendshipAction.ADD)
        .build();
    blockingStub.updateUserFriendship(request);

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException();
  }

  @DisplayName("Принять заявку в друзья поторно")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptTwiceFriendshipRequestTest(User user, @Friends User[] friends) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(friends[0].getId())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    blockingStub.updateUserFriendship(request);

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException();
  }

  @DisplayName("Отклонить ранее принятую заявку в друзья поторно")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectTwiceFriendshipRequestTest(User user, @Friends User[] friends) {
    final UpdateUserFriendshipRequest acceptRequest = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(friends[0].getId())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    blockingStub.updateUserFriendship(acceptRequest);

    final UpdateUserFriendshipRequest rejectRequest = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(friends[0].getId())
        .setAction(FriendshipAction.REJECT)
        .build();

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(rejectRequest))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(friends[0].getUsername(), user.getUsername());
  }

  @DisplayName("Обновить заявку в друзья несуществующим пользователем")
  @Test
  void sentFriendshipRequestTest() {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId("00000000-0000-0000-0000-000000000000")
        .setActionTargetUserId("00000000-0000-0000-0000-000000000000")
        .setAction(FriendshipAction.ADD)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasUserNotFoundMessage(request.getActionAuthorUserId());
  }

  @DisplayName("Обновить заявку в друзья с несуществующим пользователем")
  @CreateUser
  @Test
  void sentFriendshipRequestTest(User user) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId("00000000-0000-0000-0000-000000000000")
        .setAction(FriendshipAction.ADD)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasUserNotFoundMessage(request.getActionTargetUserId());
  }
}
