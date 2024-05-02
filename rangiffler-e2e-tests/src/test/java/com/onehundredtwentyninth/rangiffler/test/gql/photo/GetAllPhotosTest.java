package com.onehundredtwentyninth.rangiffler.test.gql.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.GqlLike;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.PHOTO_LIST)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.PHOTO_LIST)})
@DisplayName("[gql] Photo")
class GetAllPhotosTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("[gql] Получение всех фото пользователя")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE, likes = 2)
      }
  )
  @Test
  void getFeedTest(@Token String token, TestUser user, @GqlRequestFile("gql/getFeed.json") GqlRequest request) {
    var response = gatewayClient.getFeed(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getFeed().getPhotos())
            .hasEdgesCount(user.getPhotos().size())
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedPhoto = user.getPhotos().get(0);
    var expectedLikes = photoRepository.findLikesByPhotoId(expectedPhoto.getId()).stream()
        .map(s -> new GqlLike(s.getUserId(), null, null))
        .toList();

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getFeed().getPhotos().getEdges().get(0))
            .hasId(expectedPhoto.getId())
            .hasSrc(expectedPhoto.getPhoto())
            .hasCountryCode(expectedPhoto.getCountry().getCode())
            .hasDescription(expectedPhoto.getDescription())
            .hasTotalLikes(2)
            .hasLikes(expectedLikes)
    );
  }

  @DisplayName("[gql] Получение всех фото пользователя и его друзей")
  @ApiLogin
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
      }, friends = {
      @Friend(photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)),
      @Friend(pending = true, photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)),
      @Friend(pending = true, friendshipRequestType = FriendshipRequestType.OUTCOME,
          photos = @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.AMSTERDAM)
      )
  }
  )
  @Test
  void getFeedWithFriendsTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/getFeedWithFriends.json") GqlRequest request) {
    var response = gatewayClient.getFeed(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getFeed().getPhotos())
            .hasEdgesCount(3)
            .hasPrevious(false)
            .hasNext(false)
    );
  }
}
