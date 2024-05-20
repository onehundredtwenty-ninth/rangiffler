package com.onehundredtwentyninth.rangiffler.test.gql.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
@DisplayName("[gql] Photo")
class DeletePhotoTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("[gql] Удаление фото")
  @ApiLogin
  @CreateUser(
      photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
  )
  @Test
  void deletePhotoTest(@Token String token, TestUser user, @GqlRequestFile("gql/deletePhoto.json") GqlRequest request) {
    request.variables().put("id", user.getPhotos().get(0).getId());
    var response = gatewayClient.deletePhoto(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    Assertions.assertThat(response.getData().getDeletePhoto())
        .describedAs("Поле ответа deletePhoto true")
        .isTrue();

    Assertions.assertThatNoException()
        .describedAs("Фото с id %s отсутствует в БД", user.getPhotos().get(0).getId())
        .isThrownBy(() ->
            Awaitility.await("Ожидаем удаления фото из БД")
                .atMost(Duration.ofMillis(10000))
                .pollInterval(Duration.ofMillis(1000))
                .ignoreExceptions()
                .until(() -> photoRepository.findPhotoById(user.getPhotos().get(0).getId()).isEmpty())
        );
  }
}
