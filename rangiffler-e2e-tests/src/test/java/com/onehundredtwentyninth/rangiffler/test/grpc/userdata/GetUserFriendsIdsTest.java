package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.grpc.UserIdsResponse;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GrpcTest
@Epic(Epics.USERS)
@Feature(Features.USER_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.USERS), @Tag(Features.USER_LIST)})
class GetUserFriendsIdsTest {

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

  @DisplayName("Получение id всех друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getAllUserFriendsIdsTest(User user, User[] friends) {
    final UserRequest request = UserRequest.newBuilder()
        .setUsername(user.getUsername())
        .build();
    final UserIdsResponse response = blockingStub.getUserFriendsIds(request);

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response)
          .describedAs("Ответ не null")
          .isNotNull();

      softAssertions.assertThat(response.getUserIdsCount())
          .describedAs("Количество id соответствует количеству созданных друзей")
          .isEqualTo(friends.length);

      softAssertions.assertThat(response.getUserIdsList())
          .describedAs("id соответствуют id созданных друзей")
          .containsExactly(friends[0].getId(), friends[1].getId());
    });
  }

  @DisplayName("Отсутствие id неподтверженных друзей в списке id друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend(pending = true),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getAllUserFriendsIdsWithoutPendingTest(User user, User[] friends) {
    final UserRequest request = UserRequest.newBuilder()
        .setUsername(user.getUsername())
        .build();
    final UserIdsResponse response = blockingStub.getUserFriendsIds(request);

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response)
          .describedAs("Ответ не null")
          .isNotNull();

      softAssertions.assertThat(response.getUserIdsCount())
          .describedAs("Количество id соответствует количеству созданных друзей")
          .isEqualTo(1);

      softAssertions.assertThat(response.getUserIdsList())
          .describedAs("id соответствуют id созданных друзей")
          .containsExactly(friends[0].getId());
    });
  }
}
