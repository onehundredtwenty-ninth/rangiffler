package com.onehundredtwentyninth.rangiffler.test.gql;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.GqlLike;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.LikeInput;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.PhotoInput;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
class LikePhotoTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private PhotoRepository photoRepository;

  @DisplayName("Лайк фото пользователя")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(photos = {
              @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
          })
      }
  )
  @Test
  void likePhotoTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/likePhoto.json") GqlRequest request) {
    var photoId = user.getFriends().get(0).getPhotos().get(0).getId();
    var input = new PhotoInput();
    input.setId(photoId);
    input.setLike(new LikeInput(user.getId()));
    request.variables().put("input", input);

    final var response = gatewayClient.updatePhoto(token, request);

    final var expectedLikes = photoRepository.findLikesByPhotoId(photoId)
        .stream()
        .map(s -> new GqlLike(s.getUserId(), null, null))
        .toList();

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getPhoto())
            .hasId(photoId)
            .hasTotalLikes(1)
            .hasLikes(expectedLikes)
    );
  }

  @DisplayName("Снять лайк с фото пользователя")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend(photos = {
              @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
          })
      }
  )
  @Test
  void rejectLikePhotoTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/likePhoto.json") GqlRequest request) {
    var photoId = user.getFriends().get(0).getPhotos().get(0).getId();
    var input = new PhotoInput();
    input.setId(photoId);
    input.setLike(new LikeInput(user.getId()));
    request.variables().put("input", input);

    gatewayClient.updatePhoto(token, request);
    final var response = gatewayClient.updatePhoto(token, request);
    final var expectedLikes = photoRepository.findLikesByPhotoId(photoId);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getPhoto())
            .hasId(photoId)
            .hasTotalLikes(0)
    );

    Assertions.assertThat(expectedLikes)
        .isEmpty();
  }
}