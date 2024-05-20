package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcStatusExceptionAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.grpc.UpdateUserFriendshipRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[grpc] Userdata")
class IllegalUpdateUserFriendshipTest extends GrpcUserdataTestBase {

  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[grpc] Удалить несуществующую заявку в друзья")
  @CreateUser
  @Test
  void deleteNonExistentFriendshipRequestTest(TestUser user, @Extras TestUser[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(users[0].getId().toString())
        .setAction(FriendshipAction.DELETE)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipNotFoundMessage(user.getUsername(), users[0].getUsername());
  }

  @DisplayName("[grpc] Принять собственную заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void acceptNonExistentFriendshipRequestTest(TestUser user) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getOutcomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(user.getOutcomeInvitations().get(0).getUsername(), user.getUsername());

    final var friendshipEntity = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getId(),
        user.getOutcomeInvitations().get(0).getId()
    );
    Assertions.assertThat(friendshipEntity.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья не изменился")
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("[grpc] Отклонить собственную заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void rejectNonExistentFriendshipRequestTest(TestUser user) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getOutcomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.REJECT)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(user.getOutcomeInvitations().get(0).getUsername(), user.getUsername());

    final var friendshipEntity = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getId(),
        user.getOutcomeInvitations().get(0).getId()
    );
    Assertions.assertThat(friendshipEntity.orElseThrow().getStatus())
        .describedAs("Статус заявки в друзья не изменился")
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("[grpc] Отправка GqlFriendshipAction UNSPECIFIED")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void unspecifiedFriendshipActionTest(TestUser user) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getOutcomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.UNSPECIFIED)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasAbortedMessage();
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[grpc] Отправить повторно заявку в друзья")
  @CreateUser
  @Test
  void sentSecondFriendshipRequestTest(TestUser user, @Extras TestUser[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(users[0].getId().toString())
        .setAction(FriendshipAction.ADD)
        .build();
    blockingStub.updateUserFriendship(request);

    Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getId(),
                users[0].getId()
            ).isPresent()
        );

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .messageContains(
            "ERROR: duplicate key value violates unique constraint \"friendship_requester_id_addressee_id_key\""
        );
  }

  @DisplayName("[grpc] Принять заявку в друзья повторно")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptTwiceFriendshipRequestTest(TestUser user) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getIncomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    blockingStub.updateUserFriendship(request);

    Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getIncomeInvitations().get(0).getId(),
                user.getId(),
                FriendshipStatus.ACCEPTED
            ).isPresent()
        );

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(user.getIncomeInvitations().get(0).getUsername(), user.getUsername());
  }

  @DisplayName("[grpc] Отклонить ранее принятую заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectTwiceFriendshipRequestTest(TestUser user) {
    final UpdateUserFriendshipRequest acceptRequest = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getIncomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    blockingStub.updateUserFriendship(acceptRequest);

    Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
                user.getIncomeInvitations().get(0).getId(),
                user.getId(),
                FriendshipStatus.ACCEPTED
            ).isPresent()
        );

    final UpdateUserFriendshipRequest rejectRequest = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getIncomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.REJECT)
        .build();

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(rejectRequest))
        .isInstanceOfStatusRuntimeException()
        .hasFriendshipRequestNotFoundMessage(user.getIncomeInvitations().get(0).getUsername(), user.getUsername());
  }

  @DisplayName("[grpc] Обновить заявку в друзья несуществующим пользователем")
  @Test
  void sentFriendshipRequestTest() {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(new UUID(0, 0).toString())
        .setActionTargetUserId(new UUID(0, 0).toString())
        .setAction(FriendshipAction.ADD)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasUserNotFoundMessage(request.getActionAuthorUserId());
  }

  @DisplayName("[grpc] Обновить заявку в друзья с несуществующим пользователем")
  @CreateUser
  @Test
  void sentFriendshipRequestTest(TestUser user) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(new UUID(0, 0).toString())
        .setAction(FriendshipAction.ADD)
        .build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updateUserFriendship(request))
        .isInstanceOfStatusRuntimeException()
        .hasUserNotFoundMessage(request.getActionTargetUserId());
  }
}
