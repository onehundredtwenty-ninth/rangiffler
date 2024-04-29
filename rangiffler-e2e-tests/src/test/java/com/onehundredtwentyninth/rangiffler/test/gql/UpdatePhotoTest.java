package com.onehundredtwentyninth.rangiffler.test.gql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.PhotoInput;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
class UpdatePhotoTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private ObjectMapper mapper;

  @DisplayName("Обновление фото")
  @ApiLogin
  @CreateUser(
      photos = @WithPhoto(countryCode = "ca", image = "Amsterdam.png")
  )
  @Test
  void updatePhotoTest(@Token String token, TestUser user, @GqlRequestFile("gql/updatePhoto.json") GqlRequest request) {
    var photoInput = mapper.convertValue(request.variables().get("input"), PhotoInput.class);
    photoInput.setId(UUID.fromString(user.getPhotos().get(0).getId()));
    request.variables().put("input", photoInput);

    var response = gatewayClient.updatePhoto(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    var dbPhoto = photoRepository.findRequiredPhotoById(UUID.fromString(user.getPhotos().get(0).getId()));
    var country = countryRepository.findCountryByCode(photoInput.getCountry().code());

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getPhoto())
            .hasId(UUID.fromString(user.getPhotos().get(0).getId()))
            .hasSrc(user.getPhotos().get(0).getSrc().toByteArray())
            .hasCountryCode(photoInput.getCountry().code())
            .hasDescription(photoInput.getDescription())
            .hasTotalLikes(0)
    );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(dbPhoto)
            .hasId(user.getPhotos().get(0).getId())
            .hasUserId(user.getId().toString())
            .hasCountryId(country.getId().toString())
            .hasDescription(photoInput.getDescription())
    );
  }
}
