package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipEntity;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.UpdateUserFriendshipRequest;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GrpcTest
@Epic(Epics.USERS)
@Feature(Features.USER_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.USERS), @Tag(Features.USER_LIST)})
class UpdateUserFriendshipTest {

  @Inject
  private UserRepository userRepository;
  @Inject
  private FriendshipRepository friendshipRepository;
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

  @DisplayName("Принять заявку в друзья")
  @CreateUser(
      friends = {
          @Friend(pending = true)
      }
  )
  @Test
  void acceptFriendshipRequestTest(User user, User[] friends) {
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
  void rejectFriendshipRequestTest(User user, User[] friends) {
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
  void deleteFriendshipTest(User user, User[] friends) {
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
