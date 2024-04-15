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
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
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
class UpdatePhotoTest extends GrpcPhotoTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Изменение фото пользователя")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png")
      })
  @Test
  void updatePhotoTest(User user) {
    var country = countryRepository.findCountryByCode("ru");
    final PhotoEntity oldPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);

    final UpdatePhotoRequest request = UpdatePhotoRequest.newBuilder()
        .setUserId(user.getId())
        .setId(oldPhoto.getId().toString())
        .setSrc(ByteString.EMPTY)
        .setCountryId(country.getId().toString())
        .setDescription(UUID.randomUUID().toString())
        .build();
    final Photo response = blockingStub.updatePhoto(request);

    final PhotoEntity expectedPhoto = photoRepository.findByUserId(UUID.fromString(user.getId())).get(0);
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedPhoto.getId().toString())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryId(expectedPhoto.getCountryId().toString())
            .hasDescription(expectedPhoto.getDescription())
            .hasCreationDate(expectedPhoto.getCreatedDate().toLocalDateTime())
    );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(expectedPhoto)
            .hasId(request.getId())
            .hasUserId(request.getUserId())
            .hasSrc(request.getSrc().toByteArray())
            .hasCountryId(request.getCountryId())
            .hasDescription(request.getDescription())
    );
  }
}