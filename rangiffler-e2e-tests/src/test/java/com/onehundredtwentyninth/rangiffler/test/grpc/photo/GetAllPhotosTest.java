package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.LikeEntity;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoResponse;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.PHOTO_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.PHOTO_LIST)})
@DisplayName("[grpc] Photo")
class GetAllPhotosTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("[grpc] Получение всех фото пользователя")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE, likes = 1)
      })
  @Test
  void getAllPhotosTest(TestUser user) {
    final PhotoRequest request = PhotoRequest.newBuilder()
        .addAllUserIds(List.of(user.getId().toString()))
        .setPage(0)
        .setSize(10)
        .build();
    final PhotoResponse response = blockingStub.getPhotos(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
    );

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(user.getId()).get(0);
    List<LikeEntity> expectedLikes = photoRepository.findLikesByPhotoId(
        UUID.fromString(response.getPhotosList().get(0).getId()));

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getPhotosList().get(0))
            .hasId(expectedPhoto.getId().toString())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryId(expectedPhoto.getCountryId().toString())
            .hasDescription(expectedPhoto.getDescription())
            .hasCreationDate(expectedPhoto.getCreatedDate().toLocalDateTime())
            .hasTotalLikes(1)
            .hasLikes(expectedLikes)
    );
  }

  @DisplayName("[grpc] Получение всех фото пользователя и его друзей")
  @CreateUser(
      friends = {
          @Friend(
              photos = {
                  @WithPhoto(countryCode = CountryCodes.MX, image = PhotoFiles.FRANCE, description = "insertedDescriptionFriend"),
              }),
          @Friend(
              photos = {
                  @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM, description = "insertedDescriptionFriend2"),
              })
      },
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
      })
  @Test
  void getAllPhotosWithFriendsTest(TestUser user) {
    final PhotoRequest request = PhotoRequest.newBuilder()
        .addAllUserIds(List.of(
            user.getId().toString(),
            user.getFriends().get(0).getId().toString(),
            user.getFriends().get(1).getId().toString())
        )
        .setPage(0)
        .setSize(10)
        .build();
    final PhotoResponse response = blockingStub.getPhotos(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(3)
            .hasNext(false)
    );
  }
}
