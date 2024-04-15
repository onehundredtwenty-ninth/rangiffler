package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.grpc.UserByIdRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
class GetUserTest extends GrpcUserdataTestBase {

  @DisplayName("Получение пользователя по username")
  @CreateUser
  @Test
  void getUserByUsernameTest(User user) {
    final UserRequest request = UserRequest.newBuilder()
        .setUsername(user.getUsername())
        .build();
    final User response = blockingStub.getUser(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar().toByteArray())
            .hasCountryId(user.getCountryId())
    );
  }

  @DisplayName("Получение пользователя по id")
  @CreateUser
  @Test
  void getUserByIdTest(User user) {
    final UserByIdRequest request = UserByIdRequest.newBuilder()
        .setId(user.getId())
        .build();
    final User response = blockingStub.getUserById(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(user.getFirstname())
            .hasLastName(user.getLastName())
            .hasAvatar(user.getAvatar().toByteArray())
            .hasCountryId(user.getCountryId())
    );
  }
}
