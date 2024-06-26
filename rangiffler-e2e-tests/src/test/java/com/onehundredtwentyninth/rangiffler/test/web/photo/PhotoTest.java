package com.onehundredtwentyninth.rangiffler.test.web.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.PHOTO_LIST)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.PHOTO_LIST)})
@DisplayName("[web] Photo")
class PhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;

  @DisplayName("[web] Получение фото пользователя")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE, likes = 2)
      }
  )
  @Test
  void userPhotoTest(TestUser user) {
    myTravelsPage.open()
        .exactlyPhotoCardsShouldBePresented(user.getPhotos().toArray(new TestPhoto[0]));
  }

  @DisplayName("[web] Получение фото пользователя и его друзей")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
      }, friends = {
      @Friend(photos = @WithPhoto(countryCode = CountryCodes.MX, image = PhotoFiles.AMSTERDAM)),
      @Friend(pending = true, photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)),
      @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME,
          photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
      )
  }
  )
  @Test
  void userPhotoWithFriendsTest(TestUser user) {
    myTravelsPage.open()
        .clickWithFriendsButton()
        .photosCountShouldBeEqualTo(user.getPhotos().size() + user.getFriends().get(0).getPhotos().size())
        .photoCardsShouldBePresented(
            user.getPhotos().get(0),
            user.getPhotos().get(1),
            user.getFriends().get(0).getPhotos().get(0)
        );
  }
}
