package com.onehundredtwentyninth.rangiffler.test.web;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.PHOTO_LIST)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.PHOTO_LIST)})
class PhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;

  @DisplayName("Получение фото пользователя")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE, likes = 2)
      }
  )
  @Test
  void currentUserInfoTest(TestUser user) {
    myTravelsPage.open()
        .exactlyPhotoCardsShouldBePresented(user.getPhotos().toArray(new TestPhoto[0]));
  }
}
