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
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.WithPhoto;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
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
          @WithPhoto(countryCode = "cn", image = "France.png")
      })
  @Test
  void likePhotoTest(User user) {
    final PhotoEntity oldPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);
    final LikePhotoRequest request = LikePhotoRequest.newBuilder()
        .setUserId(user.getId())
        .setPhotoId(oldPhoto.getId().toString())
        .build();
    final Photo response = blockingStub.likePhoto(request);

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedPhoto.getId().toString())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryId(expectedPhoto.getCountryId().toString())
            .hasDescription(expectedPhoto.getDescription())
            .hasCreationDate(expectedPhoto.getCreatedDate().toLocalDateTime())
            .hasTotalLikes(1)
            .hasLikeFromUser(user.getId())
    );
  }

  @DisplayName("Снять лайк с фото пользователя")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png")
      })
  @Test
  void rejectLikePhotoTest(User user) {
    final PhotoEntity oldPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);
    final LikePhotoRequest request = LikePhotoRequest.newBuilder()
        .setUserId(user.getId())
        .setPhotoId(oldPhoto.getId().toString())
        .build();
    blockingStub.likePhoto(request);
    final Photo response = blockingStub.likePhoto(request);

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);
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
