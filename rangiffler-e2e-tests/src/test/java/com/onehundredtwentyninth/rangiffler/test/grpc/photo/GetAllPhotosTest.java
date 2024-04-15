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
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.Friends;
import com.onehundredtwentyninth.rangiffler.jupiter.WithPhoto;
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
class GetAllPhotosTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("Получение всех фото пользователя")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png", likes = 1)
      })
  @Test
  void getAllPhotosTest(User user) {
    final PhotoRequest request = PhotoRequest.newBuilder()
        .addAllUserIds(List.of(user.getId()))
        .setPage(0)
        .setSize(10)
        .build();
    final PhotoResponse response = blockingStub.getPhotos(request);

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasPageSize(1)
            .hasNext(false)
    );

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);
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

  @DisplayName("Получение всех фото пользователя и его друзей")
  @CreateUser(
      friends = {
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME,
              photos = {
                  @WithPhoto(countryCode = "mx", image = "France.png", description = "insertedDescriptionFriend"),
              }),
          @Friend(pending = true, friendshipRequestType = FriendshipRequestType.INCOME,
              photos = {
                  @WithPhoto(countryCode = "ca", image = "Amsterdam.png", description = "insertedDescriptionFriend2"),
              })
      },
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png")
      })
  @Test
  void getAllPhotosWithFriendsTest(User user, @Friends User[] friends) {
    final PhotoRequest request = PhotoRequest.newBuilder()
        .addAllUserIds(List.of(user.getId(), friends[0].getId(), friends[1].getId()))
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