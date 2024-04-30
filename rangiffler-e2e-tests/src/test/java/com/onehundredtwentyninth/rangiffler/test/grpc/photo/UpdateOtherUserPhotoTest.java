package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcStatusExceptionAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
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
class UpdateOtherUserPhotoTest extends GrpcPhotoTestBase {

  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Изменение фото другого пользователя")
  @CreateUser(
      friends = @Friend(
          photos = {
              @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
          }
      )
  )
  @Test
  void updatePhotoTest(TestUser user) {
    var country = countryRepository.findCountryByCode("ru");

    final UpdatePhotoRequest request = UpdatePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setId(user.getFriends().get(0).getPhotos().get(0).getId())
        .setCountryId(country.getId().toString())
        .setDescription(UUID.randomUUID().toString())
        .build();

    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updatePhoto(request))
        .isInstanceOfStatusRuntimeException()
        .hasPhotoPermissionDeniedMessage(user.getFriends().get(0).getPhotos().get(0).getId(), user.getId().toString());
  }
}
