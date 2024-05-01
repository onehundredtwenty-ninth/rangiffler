package com.onehundredtwentyninth.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
class LikePhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("Лайк фото")
  @ApiLogin
  @CreateUser(
      friends = @Friend(photos = @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE))
  )
  @Test
  void likePhotoTest(TestUser user) {
    var photoToLike = user.getFriends().get(0).getPhotos().get(0);
    myTravelsPage.open()
        .clickWithFriendsButton()
        .likePhoto(photoToLike);

    final var like = photoRepository.findLikesByPhotoId(photoToLike.getId());
    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(like)
          .hasSize(1);

      softAssertions.assertThat(like.get(0).getUserId())
          .isEqualTo(user.getId());
    });
  }

  @DisplayName("Снять лайк с фото")
  @ApiLogin
  @CreateUser(
      friends = @Friend(photos = @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE))
  )
  @Test
  void dislikePhotoTest(TestUser user) {
    var photoToLike = user.getFriends().get(0).getPhotos().get(0);
    myTravelsPage.open()
        .clickWithFriendsButton()
        .likePhoto(photoToLike);
    Selenide.refresh();

    myTravelsPage
        .clickWithFriendsButton()
        .dislikePhoto(photoToLike);

    final var like = photoRepository.findLikesByPhotoId(photoToLike.getId());
    Assertions.assertThat(like)
        .isEmpty();
  }
}
