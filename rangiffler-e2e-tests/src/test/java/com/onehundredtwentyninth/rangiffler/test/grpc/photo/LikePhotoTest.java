package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.LikePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
class LikePhotoTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("Лайк фото пользователя")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
      })
  @Test
  void likePhotoTest(TestUser user) {
    final LikePhotoRequest request = LikePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setPhotoId(user.getPhotos().get(0).getId())
        .build();
    final Photo response = blockingStub.likePhoto(request);

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(user.getId()).get(0);
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedPhoto.getId().toString())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryId(expectedPhoto.getCountryId().toString())
            .hasDescription(expectedPhoto.getDescription())
            .hasCreationDate(expectedPhoto.getCreatedDate().toLocalDateTime())
            .hasTotalLikes(1)
            .hasLikeFromUser(user.getId().toString())
    );
  }

  @DisplayName("Снять лайк с фото пользователя")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
      })
  @Test
  void rejectLikePhotoTest(TestUser user) {
    final LikePhotoRequest request = LikePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setPhotoId(user.getPhotos().get(0).getId())
        .build();
    blockingStub.likePhoto(request);
    final Photo response = blockingStub.likePhoto(request);

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(user.getId()).get(0);
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedPhoto.getId().toString())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryId(expectedPhoto.getCountryId().toString())
            .hasDescription(expectedPhoto.getDescription())
            .hasCreationDate(expectedPhoto.getCreatedDate().toLocalDateTime())
            .hasTotalLikes(0)
    );
  }
}
