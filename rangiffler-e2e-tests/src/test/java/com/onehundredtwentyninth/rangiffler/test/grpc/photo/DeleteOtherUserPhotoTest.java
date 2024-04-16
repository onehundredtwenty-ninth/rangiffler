package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcStatusExceptionAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.Friends;
import com.onehundredtwentyninth.rangiffler.jupiter.WithPhoto;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.DELETE_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.DELETE_PHOTO)})
class DeleteOtherUserPhotoTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Удаление фото другого пользователя")
  @CreateUser(
      friends = @Friend(
          photos = {
              @WithPhoto(countryCode = "cn", image = "France.png")
          }
      )
  )
  @Test
  void deletePhotoTest(User user, @Friends User[] friends) {
    final PhotoEntity oldPhoto = photoRepository.findByUserId(UUID.fromString(friends[0].getId())).get(0);
    final DeletePhotoRequest request = DeletePhotoRequest.newBuilder()
        .setUserId(user.getId())
        .setPhotoId(oldPhoto.getId().toString())
        .build();

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.deletePhoto(request))
        .isInstanceOfStatusRuntimeException()
        .hasPhotoPermissionDeniedMessage(oldPhoto.getId().toString(), user.getId());
  }
}
