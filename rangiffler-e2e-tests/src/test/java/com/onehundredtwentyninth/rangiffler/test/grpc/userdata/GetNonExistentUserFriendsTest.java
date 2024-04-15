package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class GetNonExistentUserFriendsTest extends GrpcUserdataTestBase {

  @DisplayName("Получение друзей пользователя по несуществующему username")
  @Test
  void getUserByNonExistentCodeTest() {
    var request = AllUsersRequest.newBuilder().setUsername("nonExistentUsername").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getUserFriends(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: User nonExistentUsername not found");
  }

  @DisplayName("Получение id друзей пользователя по несуществующему username")
  @Test
  void getUserByNonExistentIdTest() {
    var request = UserRequest.newBuilder().setUsername("nonExistentUsername").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getUserFriendsIds(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: User nonExistentUsername not found");
  }
}
