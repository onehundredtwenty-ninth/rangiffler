package com.onehundredtwentyninth.rangiffler.test.web.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.DELETE_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.DELETE_PHOTO)})
@DisplayName("[web] Photo")
class DeletePhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("[web] Удаление фото")
  @ApiLogin
  @CreateUser(
      photos = @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
  )
  @Test
  void createPhotoTest(TestUser user) {
    myTravelsPage.open()
        .deletePhoto(user.getPhotos().get(0));

    Awaitility.await("Ожидаем удаления фото из БД")
        .atMost(Duration.ofMillis(10000))
        .pollInterval(Duration.ofMillis(1000))
        .until(() -> photoRepository.findPhotoById(user.getPhotos().get(0).getId()).isEmpty());
  }
}
