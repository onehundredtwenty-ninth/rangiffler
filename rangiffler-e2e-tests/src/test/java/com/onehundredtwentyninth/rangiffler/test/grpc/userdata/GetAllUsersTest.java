package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GrpcTest
@Epic(Epics.USERS)
@Feature(Features.USER_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.USERS), @Tag(Features.USER_LIST)})
class GetAllUsersTest {

  @Inject
  private UserRepository userRepository;
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

  @DisplayName("Получение всех пользователей")
  @CreateUser
  @Test
  void getAllUsersTest(User user) {
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

    final UserEntity expectedUser = userRepository.findById(UUID.fromString(response.getAllUsersList().get(0).getId()));
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

  @DisplayName("Получение второй страницы всех пользователей")
  @CreateUser
  @Test
  void getAllUsersPageTwoTest(User user) {
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

  @DisplayName("Получение всех пользователей по переданному username")
  @CreateUser
  @Test
  void getAllUsersWithUsernameSearchTest(User user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery("bee")
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .hasUserWithUsername("bee")
    );

    final UserEntity expectedUser = userRepository.findByUsername("bee");
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

  @DisplayName("Получение пользователей при передаче SearchQuery username автора запроса")
  @CreateUser
  @Test
  void getAllUsersWithCurrentUserUsernameSearchTest(User user) {
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

  @DisplayName("Получение всех пользователей по переданному firstname")
  @CreateUser
  @Test
  void getAllUsersWithFirstnameSearchTest(User user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery("Tarra")
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .hasUserWithFirstName("Tarra")
    );

    final UserEntity expectedUser = userRepository.findByFirstname("Tarra");
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

  @DisplayName("Получение всех пользователей по переданному lastName")
  @CreateUser
  @Test
  void getAllUsersWithLastnameSearchTest(User user) {
    final AllUsersRequest request = AllUsersRequest.newBuilder()
        .setUsername(user.getUsername())
        .setSearchQuery("Batz")
        .setPage(0)
        .setSize(10)
        .build();
    final AllUsersResponse response = blockingStub.getAllUsers(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
            .hasUserWithLastName("Batz")
    );

    final UserEntity expectedUser = userRepository.findByLastname("Batz");
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
