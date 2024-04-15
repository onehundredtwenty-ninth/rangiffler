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
import com.onehundredtwentyninth.rangiffler.jupiter.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friends;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class UpdateUserFriendshipTest extends GrpcUserdataTestBase {

  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("Отправить заявку в друзья")
  @CreateUser
  @Test
  void sentFriendshipRequestTest(User user, @Extras User[] users) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(users[0].getId())
        .setAction(FriendshipAction.ADD)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar().toByteArray())
            .hasCountryId(user.getCountryId())
    );

    final FriendshipEntity friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        UUID.fromString(user.getId()), UUID.fromString(users[0].getId())).orElseThrow();
    Assertions.assertThat(friendship.getStatus())
        .isEqualTo(FriendshipStatus.PENDING);
  }

  @DisplayName("Принять заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptFriendshipRequestTest(User user, @Friends User[] friends) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(friends[0].getId())
        .setAction(FriendshipAction.ACCEPT)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar().toByteArray())
            .hasCountryId(user.getCountryId())
    );

    final FriendshipEntity friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        UUID.fromString(friends[0].getId()), UUID.fromString(user.getId())).orElseThrow();
    Assertions.assertThat(friendship.getStatus())
        .isEqualTo(FriendshipStatus.ACCEPTED);
  }

  @DisplayName("Отклонить заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void rejectFriendshipRequestTest(User user, @Friends User[] friends) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(friends[0].getId())
        .setAction(FriendshipAction.REJECT)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar().toByteArray())
            .hasCountryId(user.getCountryId())
    );

    final Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        UUID.fromString(friends[0].getId()), UUID.fromString(user.getId())
    );
    Assertions.assertThat(friendship)
        .describedAs("Заявка на дружбу отсутствует в БД")
        .isEmpty();
  }

  @DisplayName("Удаление из друзей")
  @CreateUser(
      friends = {
          @Friend
      }
  )
  @Test
  void deleteFriendshipTest(User user, @Friends User[] friends) {
    final UpdateUserFriendshipRequest request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(user.getId())
        .setActionTargetUserId(friends[0].getId())
        .setAction(FriendshipAction.DELETE)
        .build();
    final User response = blockingStub.updateUserFriendship(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar().toByteArray())
            .hasCountryId(user.getCountryId())
    );

    final Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByRequesterIdAndAddresseeId(
        UUID.fromString(friends[0].getId()), UUID.fromString(user.getId())
    );
    Assertions.assertThat(friendship)
        .describedAs("Запись о дружбе отсутствует в БД")
        .isEmpty();
  }
}
