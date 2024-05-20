package com.onehundredtwentyninth.rangiffler.test.web.photo;

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
import com.onehundredtwentyninth.rangiffler.model.testdata.TestPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import com.onehundredtwentyninth.rangiffler.utils.ImageUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
@DisplayName("[web] Photo")
class AddPhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private PhotoRepository photoRepository;
  private PhotoEntity createdPhoto;

  @DisplayName("[web] Добавление фото")
  @ApiLogin
  @CreateUser
  @Test
  void createPhotoTest(TestUser user) {
    final var country = countryRepository.findRequiredCountryByCode("py");
    final var photoToCreate = TestPhoto.builder()
        .photo(ImageUtils.getImageFromResourceAsBase64("Amsterdam.png").getBytes(StandardCharsets.UTF_8))
        .country(CountryMapper.toTestCountry(country))
        .description(UUID.randomUUID().toString())
        .likes(Collections.emptyList())
        .build();

    myTravelsPage.open()
        .addPhoto("image/Amsterdam.png", photoToCreate.getCountry().getCode(), photoToCreate.getDescription());

    final var userPhotos = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> photoRepository.findByUserId(user.getId()),
            photoEntities -> !photoEntities.isEmpty()
        );

    Assertions.assertThat(userPhotos)
        .describedAs("Количество фото пользователя равно 1")
        .hasSize(1);
    createdPhoto = userPhotos.get(0);

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(createdPhoto)
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
