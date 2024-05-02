package com.onehundredtwentyninth.rangiffler.test.web;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.UUID;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
class UpdatePhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("Изменение фото")
  @ApiLogin
  @CreateUser(
      photos = @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
  )
  @Test
  void createPhotoTest(TestUser user) {
    final var country = countryRepository.findCountryByCode("ru");
    final var newDescription = UUID.randomUUID().toString();

    myTravelsPage.open()
        .editPhoto(user.getPhotos().get(0), country.getCode(), newDescription);

    final var userPhotos = Awaitility.await("Ожидаем обновления фото в БД")
        .atMost(Duration.ofMillis(10000))
        .pollInterval(Duration.ofMillis(1000))
        .until(
            () -> photoRepository.findByUserId(user.getId()),
            photoEntities -> newDescription.equals(photoEntities.get(0).getDescription())
        );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(userPhotos.get(0))
            .hasUserId(user.getId().toString())
            .hasSrc(user.getPhotos().get(0).getPhoto())
            .hasCountryId(country.getId().toString())
            .hasDescription(newDescription)
    );
  }
}
