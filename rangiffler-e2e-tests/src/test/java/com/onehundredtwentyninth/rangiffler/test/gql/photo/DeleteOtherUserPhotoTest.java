package com.onehundredtwentyninth.rangiffler.test.gql.photo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
@DisplayName("[gql] Photo")
class DeleteOtherUserPhotoTest {

  @Inject
  private GatewayClient gatewayClient;

  @DisplayName("[gql] Удаление фото другого пользователя")
  @ApiLogin
  @CreateUser(
      friends = @Friend(
          photos = {
              @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE)
          }
      )
  )
  @Test
  void deletePhotoTest(@Token String token, TestUser user, @GqlRequestFile("gql/deletePhoto.json") GqlRequest request) {
    var photo = user.getFriends().get(0).getPhotos().get(0);
    request.variables().put("id", photo.getId());

    var response = gatewayClient.deletePhoto(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasPhotoPermissionDeniedMessage(photo.getId(), user.getId())
              .hasPath(List.of("deletePhoto"))
              .hasInternalErrorExtension();
        }
    );
  }
}
