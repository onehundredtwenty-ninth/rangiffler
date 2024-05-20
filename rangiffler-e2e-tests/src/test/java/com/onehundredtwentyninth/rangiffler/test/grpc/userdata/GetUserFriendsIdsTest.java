package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.UserIdsResponse;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[grpc] Userdata")
class GetUserFriendsIdsTest extends GrpcUserdataTestBase {

  @DisplayName("[grpc] Получение id всех друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getAllUserFriendsIdsTest(TestUser user) {
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
          .isEqualTo(user.getFriends().size());

      softAssertions.assertThat(response.getUserIdsList())
          .describedAs("id соответствуют id созданных друзей")
          .containsExactly(user.getFriends().get(0).getId().toString(), user.getFriends().get(1).getId().toString());
    });
  }

  @DisplayName("[grpc] Отсутствие id неподтверженных друзей в списке id друзей пользователя")
  @CreateUser(
      friends = {
          @Friend,
          @Friend(pending = true),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME)
      }
  )
  @Test
  void getAllUserFriendsIdsWithoutPendingTest(TestUser user) {
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
          .containsExactly(user.getFriends().get(0).getId().toString());
    });
  }
}
