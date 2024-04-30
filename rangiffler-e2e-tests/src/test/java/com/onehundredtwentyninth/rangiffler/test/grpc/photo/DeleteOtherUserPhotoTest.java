package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.onehundredtwentyninth.rangiffler.assertion.GrpcStatusExceptionAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
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
@Feature(Features.DELETE_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.DELETE_PHOTO)})
class DeleteOtherUserPhotoTest extends GrpcPhotoTestBase {

  @DisplayName("Удаление фото другого пользователя")
  @CreateUser(
      friends = @Friend(
          photos = {
              @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
          }
      )
  )
  @Test
  void deletePhotoTest(TestUser user) {
    final DeletePhotoRequest request = DeletePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setPhotoId(user.getFriends().get(0).getPhotos().get(0).getId())
        .build();

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.deletePhoto(request))
        .isInstanceOfStatusRuntimeException()
        .hasPhotoPermissionDeniedMessage(user.getFriends().get(0).getPhotos().get(0).getId(), user.getId().toString());
  }
}
