package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.github.javafaker.Faker;
import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
class UpdateUserTest extends GrpcUserdataTestBase {

  @Inject
  private CountryRepository countryRepository;
  @Inject
  private UserRepository userRepository;
  private final Faker faker = new Faker();

  @DisplayName("Обновление данных пользователя")
  @CreateUser
  @Test
  void updateUserTest(User user) {
    final CountryEntity newCountry = countryRepository.findCountryByIdNot(UUID.fromString(user.getCountryId()));
    final User updateUserRequest = User.newBuilder()
        .setUsername(user.getUsername())
        .setFirstname(faker.name().firstName())
        .setLastName(faker.name().lastName())
        .setAvatar(ByteString.copyFrom(new byte[]{}))
        .setCountryId(newCountry.getId().toString())
        .build();
    final User response = blockingStub.updateUser(updateUserRequest);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(updateUserRequest.getUsername())
            .hasFirstName(updateUserRequest.getFirstname())
            .hasLastName(updateUserRequest.getLastName())
            .hasAvatar(updateUserRequest.getAvatar().toByteArray())
            .hasCountryId(updateUserRequest.getCountryId())
    );

    final UserEntity userEntity = userRepository.findById(UUID.fromString(user.getId()));
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(userEntity.getId().toString())
            .hasUsername(userEntity.getUsername())
            .hasFirstName(userEntity.getFirstname())
            .hasLastName(userEntity.getLastName())
            .hasAvatar(userEntity.getAvatar())
            .hasCountryId(userEntity.getCountryId().toString())
    );
  }

  @DisplayName("Отсутствие возможности изменения id пользователя")
  @CreateUser
  @Test
  void updateUserIdImpossibleTest(User user) {
    final User updateUserRequest = User.newBuilder()
        .setId(UUID.randomUUID().toString())
        .setUsername(user.getUsername())
        .setCountryId(user.getCountryId())
        .build();
    final User response = blockingStub.updateUser(updateUserRequest);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(user.getId())
            .hasUsername(updateUserRequest.getUsername())
    );

    final UserEntity userEntity = userRepository.findByUsername(user.getUsername());
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(userEntity.getId().toString())
            .hasUsername(userEntity.getUsername())
    );
  }

  @DisplayName("Обновление несуществующего пользователя")
  @Test
  void updateNonExistentUserTest() {
    final User updateUserRequest = User.newBuilder()
        .setUsername(faker.name().username())
        .build();
    Assertions.assertThatThrownBy(() -> blockingStub.updateUser(updateUserRequest))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: User " + updateUserRequest.getUsername() + " not found");
  }
}
