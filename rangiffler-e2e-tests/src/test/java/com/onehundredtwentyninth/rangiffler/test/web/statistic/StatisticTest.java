package com.onehundredtwentyninth.rangiffler.test.web.statistic;

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
@DisplayName("[web] Statistic")
class StatisticTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;

  @DisplayName("[web] Получение статистики фото пользователя")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE, likes = 2)
      }
  )
  @Test
  void userPhotoTest() {
    myTravelsPage.open()
        .statisticShouldBePresented(CountryCodes.CN.getCode(), "China", 1);
  }

  @DisplayName("[web] Получение статистики фото пользователя и его друзей")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.AMSTERDAM)
      }, friends = {
      @Friend(photos = @WithPhoto(countryCode = CountryCodes.MX, image = PhotoFiles.AMSTERDAM)),
      @Friend(pending = true, photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)),
      @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME,
          photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
      )
  }
  )
  @Test
  void userPhotoWithFriendsTest() {
    myTravelsPage.open()
        .clickWithFriendsButton()
        .statisticShouldBePresented(CountryCodes.CN.getCode(), "China", 2)
        .statisticShouldBePresented(CountryCodes.MX.getCode(), "Mexico", 1);
  }
}
