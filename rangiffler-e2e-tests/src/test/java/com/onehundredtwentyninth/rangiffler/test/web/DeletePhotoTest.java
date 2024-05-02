package com.onehundredtwentyninth.rangiffler.test.web;

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
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.DELETE_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.DELETE_PHOTO)})
class DeletePhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("Удаление фото")
  @ApiLogin
  @CreateUser(
      photos = @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
  )
  @Test
  void createPhotoTest(TestUser user) {
    myTravelsPage.open()
        .deletePhoto(user.getPhotos().get(0));

    final var userPhotos = photoRepository.findPhotoById(user.getPhotos().get(0).getId());
    Assertions.assertThat(userPhotos)
        .isEmpty();
  }
}
