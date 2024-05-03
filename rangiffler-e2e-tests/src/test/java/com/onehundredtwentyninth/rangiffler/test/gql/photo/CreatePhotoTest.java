package com.onehundredtwentyninth.rangiffler.test.gql.photo;

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
import com.onehundredtwentyninth.rangiffler.model.GqlPhotoResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.GqlResponse;
import com.onehundredtwentyninth.rangiffler.model.PhotoInput;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.utils.ImageUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
@DisplayName("[gql] Photo")
class CreatePhotoTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private ObjectMapper mapper;
  private GqlResponse<GqlPhotoResponse> response;

  @DisplayName("[gql] Добавление фото")
  @ApiLogin
  @CreateUser
  @Test
  void createPhotoTest(@Token String token, TestUser user, @GqlRequestFile("gql/updatePhoto.json") GqlRequest request) {
    var photoInput = mapper.convertValue(request.variables().get("input"), PhotoInput.class);
    photoInput.setSrc(ImageUtils.getImageFromResourceAsBase64("Amsterdam.png"));
    request.variables().put("input", photoInput);

    response = gatewayClient.updatePhoto(token, request);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getPhoto())
            .idIsNotNull()
            .hasSrc(photoInput.getSrc().getBytes(StandardCharsets.UTF_8))
            .hasCountryCode(photoInput.getCountry().code())
            .hasDescription(photoInput.getDescription())
            .hasTotalLikes(0)
    );

    final var country = countryRepository.findRequiredCountryByCode(photoInput.getCountry().code());
    final var expectedPhoto = photoRepository.findByUserId(user.getId()).get(0);
    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(expectedPhoto)
            .hasUserId(user.getId().toString())
            .hasSrc(photoInput.getSrc().getBytes(StandardCharsets.UTF_8))
            .hasCountryId(country.getId().toString())
            .hasDescription(photoInput.getDescription())
    );
  }

  @AfterEach
  void after() {
    photoRepository.deletePhoto(response.getData().getPhoto().getId());
  }
}
