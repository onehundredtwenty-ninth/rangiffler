package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.CreatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.DELETE_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.DELETE_PHOTO)})
class DeletePhotoTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Удалить фото")
  @CreateUser
  @Test
  void deletePhotoTest(User user) {
    var country = countryRepository.findCountryByCode("ru");
    final CreatePhotoRequest createPhotoRequest = CreatePhotoRequest.newBuilder()
        .setUserId(user.getId())
        .setSrc(ByteString.EMPTY)
        .setCountryId(country.getId().toString())
        .setDescription(UUID.randomUUID().toString())
        .build();
    final Photo createPhotoResponse = blockingStub.createPhoto(createPhotoRequest);

    final DeletePhotoRequest request = DeletePhotoRequest.newBuilder()
        .setUserId(user.getId())
        .setPhotoId(createPhotoResponse.getId())
        .build();
    final BoolValue response = blockingStub.deletePhoto(request);

    final List<PhotoEntity> userPhotos = photoRepository.findByUserId(UUID.fromString(user.getId()));
    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response.getValue())
          .describedAs("Запрос на удаление фото вернул true")
          .isTrue();

      softAssertions.assertThat(userPhotos)
          .describedAs("У пользователя отсутствуют фото в БД")
          .isEmpty();
    });
  }
}