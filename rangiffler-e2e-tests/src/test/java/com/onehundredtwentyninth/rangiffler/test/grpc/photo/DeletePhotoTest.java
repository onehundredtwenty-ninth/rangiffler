package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.google.protobuf.BoolValue;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.DELETE_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.DELETE_PHOTO)})
@DisplayName("[grpc] Photo")
class DeletePhotoTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("[grpc] Удалить фото")
  @CreateUser(
      photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
  )
  @Test
  void deletePhotoTest(TestUser user) {
    final DeletePhotoRequest request = DeletePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setPhotoId(user.getPhotos().get(0).getId().toString())
        .build();
    final BoolValue response = blockingStub.deletePhoto(request);

    final List<PhotoEntity> userPhotos = photoRepository.findByUserId(user.getId());
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
