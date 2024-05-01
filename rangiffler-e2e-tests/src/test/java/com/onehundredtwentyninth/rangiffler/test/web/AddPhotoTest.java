package com.onehundredtwentyninth.rangiffler.test.web;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.mapper.CountryMapper;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.utils.ImageUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
class AddPhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private PhotoRepository photoRepository;
  private PhotoEntity createdPhoto;

  @DisplayName("Добавление фото")
  @ApiLogin
  @CreateUser
  @Test
  void createPhotoTest(TestUser user) {
    final var country = countryRepository.findCountryByCode("py");
    final var photoToCreate = TestPhoto.builder()
        .photo(ImageUtils.getImageFromResourceAsBase64("Amsterdam.png").getBytes(StandardCharsets.UTF_8))
        .country(CountryMapper.toTestCountry(country))
        .description(UUID.randomUUID().toString())
        .likes(Collections.emptyList())
        .build();

    myTravelsPage.open()
        .addPhoto("image/Amsterdam.png", photoToCreate.getCountry().getCode(), photoToCreate.getDescription())
        .photosCountShouldBeEqualTo(1)
        .photoCardsShouldBePresented(photoToCreate);

    final var userPhotos = photoRepository.findByUserId(user.getId());
    Assertions.assertThat(userPhotos)
        .hasSize(1);
    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(userPhotos.get(0))
            .hasUserId(user.getId().toString())
            .hasSrc(photoToCreate.getPhoto())
            .hasCountryId(country.getId().toString())
            .hasDescription(photoToCreate.getDescription())
    );
  }

  @AfterEach
  void after() {
    photoRepository.deletePhoto(createdPhoto.getId());
  }
}
