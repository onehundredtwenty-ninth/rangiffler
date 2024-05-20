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
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.MyTravelsPage;
import com.onehundredtwentyninth.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.ADD_PHOTO)
@Tags({@Tag(Layers.UI), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.ADD_PHOTO)})
@DisplayName("[web] Photo")
class LikePhotoTest extends BaseWebTest {

  @Inject
  private MyTravelsPage myTravelsPage;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("[web] Лайк фото")
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

    final var likes = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> photoRepository.findLikesByPhotoId(photoToLike.getId()),
            photoLikes -> !photoLikes.isEmpty()
        );

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(likes)
          .describedAs("У фото есть один лайк")
          .hasSize(1);

      softAssertions.assertThat(likes.get(0).getUserId())
          .describedAs("UserId лайка совпадает с id текущего пользователя")
          .isEqualTo(user.getId());
    });
  }

  @DisplayName("[web] Снять лайк с фото")
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

    Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(() -> !photoRepository.findLikesByPhotoId(photoToLike.getId()).isEmpty());

    myTravelsPage.dislikePhoto(photoToLike);

    Assertions.assertThatNoException()
        .describedAs("У фото с id %s отсутствуют лайки в БД", photoToLike.getId())
        .isThrownBy(() ->
            Awaitility.await("Ожидаем удаления лайка фото из БД")
                .atMost(Duration.ofMillis(10000))
                .pollInterval(Duration.ofMillis(1000))
                .ignoreExceptions()
                .until(() -> photoRepository.findLikesByPhotoId(photoToLike.getId()).isEmpty())
        );
  }
}
