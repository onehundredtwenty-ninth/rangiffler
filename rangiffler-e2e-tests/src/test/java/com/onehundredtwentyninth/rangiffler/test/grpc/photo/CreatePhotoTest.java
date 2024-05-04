package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.CreatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
@DisplayName("[grpc] Photo")
class CreatePhotoTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;
  private Photo response;

  @DisplayName("[grpc] Добавление фото")
  @CreateUser
  @Test
  void createPhotoTest(TestUser user) {
    var country = countryRepository.findRequiredCountryByCode("ru");
    final CreatePhotoRequest request = CreatePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setSrc(ByteString.EMPTY)
        .setCountryId(country.getId().toString())
        .setDescription(UUID.randomUUID().toString())
        .build();
    response = blockingStub.createPhoto(request);

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(user.getId()).get(0);
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedPhoto.getId().toString())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryId(expectedPhoto.getCountryId().toString())
            .hasDescription(expectedPhoto.getDescription())
            .hasCreationDate(expectedPhoto.getCreatedDate().toInstant())
            .hasTotalLikes(0)
    );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(expectedPhoto)
            .hasUserId(request.getUserId())
            .hasSrc(request.getSrc().toByteArray())
            .hasCountryId(request.getCountryId())
            .hasDescription(request.getDescription())
    );
  }

  @AfterEach
  void deletePhoto() {
    photoRepository.deletePhoto(UUID.fromString(response.getId()));
  }
}
