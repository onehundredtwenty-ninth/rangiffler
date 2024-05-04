package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipEntity;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.FriendStatus;
import com.onehundredtwentyninth.rangiffler.grpc.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.grpc.UpdateUserFriendshipRequest;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[grpc] Userdata")
class UpdateUserFriendshipTest extends GrpcUserdataTestBase {

  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[grpc] Отправить заявку в друзья")
  @CreateUser
  @Test
  void sentFriendshipRequestTest(TestUser user, @Extras TestUser[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(users[0].getId().toString())
        .setAction(FriendshipAction.ADD)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(users[0].getId().toString())
            .hasUsername(users[0].getUsername())
            .hasFirstName(users[0].getFirstname())
            .hasLastName(users[0].getLastName())
            .hasAvatar(users[0].getAvatar())
            .hasCountryId(users[0].getCountry().getId().toString())
            .hasFriendStatus(FriendStatus.INVITATION_SENT)
    );

    final FriendshipEntity friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getId(), users[0].getId()).orElseThrow();
    Assertions.assertThat(friendship.getStatus())
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("[grpc] Принять заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptFriendshipRequestTest(TestUser user) {
    var actionTargetUser = user.getIncomeInvitations().get(0);
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(actionTargetUser.getId().toString())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(actionTargetUser.getId().toString())
            .hasUsername(actionTargetUser.getUsername())
            .hasFirstName(actionTargetUser.getFirstname())
            .hasLastName(actionTargetUser.getLastName())
            .hasAvatar(actionTargetUser.getAvatar())
            .hasCountryId(actionTargetUser.getCountry().getId().toString())
            .hasFriendStatus(FriendStatus.FRIEND)
    );

    final FriendshipEntity friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getIncomeInvitations().get(0).getId(), user.getId()).orElseThrow();
    Assertions.assertThat(friendship.getStatus())
        .isEqualTo(FriendshipStatus.ACCEPTED);
  }

  @DisplayName("[grpc] Отклонить заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectFriendshipRequestTest(TestUser user) {
    var actionTargetUser = user.getIncomeInvitations().get(0);
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(actionTargetUser.getId().toString())
        .setAction(FriendshipAction.REJECT)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(actionTargetUser.getId().toString())
            .hasUsername(actionTargetUser.getUsername())
            .hasFirstName(actionTargetUser.getFirstname())
            .hasLastName(actionTargetUser.getLastName())
            .hasAvatar(actionTargetUser.getAvatar())
            .hasCountryId(actionTargetUser.getCountry().getId().toString())
            .hasFriendStatus(FriendStatus.NOT_FRIEND)
    );

    final Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getIncomeInvitations().get(0).getId(), user.getId()
    );
    Assertions.assertThat(friendship)
        .describedAs("Заявка на дружбу отсутствует в БД")
        .isEmpty();
  }

  @DisplayName("[grpc] Удаление из друзей")
  @CreateUser(
      friends = {
          @Friend
      }
  )
  @Test
  void deleteFriendshipTest(TestUser user) {
    var actionTargetUser = user.getFriends().get(0);
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(actionTargetUser.getId().toString())
        .setAction(FriendshipAction.DELETE)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(actionTargetUser.getId().toString())
            .hasUsername(actionTargetUser.getUsername())
            .hasFirstName(actionTargetUser.getFirstname())
            .hasLastName(actionTargetUser.getLastName())
            .hasAvatar(actionTargetUser.getAvatar())
            .hasCountryId(actionTargetUser.getCountry().getId().toString())
            .hasFriendStatus(FriendStatus.NOT_FRIEND)
    );

    final Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getFriends().get(0).getId(), user.getId()
    );
    Assertions.assertThat(friendship)
        .describedAs("Запись о дружбе отсутствует в БД")
        .isEmpty();
  }
}
