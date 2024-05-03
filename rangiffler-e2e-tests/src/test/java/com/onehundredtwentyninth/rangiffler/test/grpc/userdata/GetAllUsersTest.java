package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_LIST)})
@DisplayName("[grpc] Userdata")
class GetAllUsersTest extends GrpcUserdataTestBase {

  @Inject
  private UserRepository userRepository;

  @DisplayName("[grpc] Получение всех пользователей")
  @CreateUser
  @Test
  void getAllUsersTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(10)
            .hasNext(true)
    );

    final UserEntity expectedUser = userRepository.findRequiredById(UUID.fromString(response.getAllUsersList().get(0).getId()));
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getAllUsersList().get(0))
            .hasId(expectedUser.getId().toString())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
            .hasCountryId(expectedUser.getCountryId().toString())
    );
  }

  @DisplayName("[grpc] Получение второй страницы всех пользователей")
  @CreateUser
  @Test
  void getAllUsersPageTwoTest(TestUser user) {
    var count = userRepository.count();
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setPage(1)
        .setSize(count)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(0)
            .hasNext(false)
    );
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[grpc] Получение всех пользователей по переданному username")
  @CreateUser
  @Test
  void getAllUsersWithUsernameSearchTest(TestUser user, @Extras TestUser[] users) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(users[0].getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .hasUserWithUsername(users[0].getUsername())
    );

    final UserEntity expectedUser = userRepository.findRequiredByUsername(users[0].getUsername());
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getAllUsersList().get(0))
            .hasId(expectedUser.getId().toString())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
            .hasCountryId(expectedUser.getCountryId().toString())
    );
  }

  @DisplayName("[grpc] Получение пользователей при передаче SearchQuery username автора запроса")
  @CreateUser
  @Test
  void getAllUsersWithCurrentUserUsernameSearchTest(TestUser user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(user.getUsername())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(0)
            .hasNext(false)
            .hasNotUserWithUsername(user.getUsername())
    );
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[grpc] Получение всех пользователей по переданному firstname")
  @CreateUser
  @Test
  void getAllUsersWithFirstnameSearchTest(TestUser user, @Extras TestUser[] users) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(users[0].getFirstname())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .hasUserWithFirstName(users[0].getFirstname())
    );

    final UserEntity expectedUser = userRepository.findRequiredByFirstname(users[0].getFirstname());
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getAllUsersList().get(0))
            .hasId(expectedUser.getId().toString())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
            .hasCountryId(expectedUser.getCountryId().toString())
    );
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[grpc] Получение всех пользователей по переданному lastName")
  @CreateUser
  @Test
  void getAllUsersWithLastnameSearchTest(TestUser user, @Extras TestUser[] users) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery(users[0].getLastName())
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .hasUserWithLastName(users[0].getLastName())
    );

    final UserEntity expectedUser = userRepository.findRequiredByLastname(users[0].getLastName());
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getAllUsersList().get(0))
            .hasId(expectedUser.getId().toString())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
            .hasCountryId(expectedUser.getCountryId().toString())
    );
  }
}
