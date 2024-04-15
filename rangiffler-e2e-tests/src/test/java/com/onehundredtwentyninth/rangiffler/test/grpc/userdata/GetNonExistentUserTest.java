package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.UserByIdRequest;
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
@Feature(Features.USER)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
class GetNonExistentUserTest extends GrpcUserdataTestBase {

  @DisplayName("Получение пользователя по несуществующему username")
  @Test
  void getUserByNonExistentCodeTest() {
    var request = UserRequest.newBuilder().setUsername("nonExistentUsername").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getUser(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: User nonExistentUsername not found");
  }

  @DisplayName("Получение пользователя по несуществующему id")
  @Test
  void getUserByNonExistentIdTest() {
    var request = UserByIdRequest.newBuilder().setId("00000000-0000-0000-0000-000000000000").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getUserById(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: User 00000000-0000-0000-0000-000000000000 not found");
  }

  @DisplayName("Получение пользователя по невалидному id")
  @Test
  void getUserByInvalidIdTest() {
    var request = UserByIdRequest.newBuilder().setId("notValidId").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getUserById(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("ABORTED: Invalid UUID string: notValidId");
  }
}
