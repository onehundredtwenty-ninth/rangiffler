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
            .hasId(user.getId().toString())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar())
            .hasCountryId(user.getCountry().getId().toString())
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
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getIncomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId().toString())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar())
            .hasCountryId(user.getCountry().getId().toString())
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
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getIncomeInvitations().get(0).getId().toString())
        .setAction(FriendshipAction.REJECT)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId().toString())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar())
            .hasCountryId(user.getCountry().getId().toString())
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
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId().toString())
        .setActionTargetUserId(user.getFriends().get(0).getId().toString())
        .setAction(FriendshipAction.DELETE)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId().toString())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar())
            .hasCountryId(user.getCountry().getId().toString())
    );

    final Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        user.getFriends().get(0).getId(), user.getId()
    );
    Assertions.assertThat(friendship)
        .describedAs("Запись о дружбе отсутствует в БД")
        .isEmpty();
  }
}
