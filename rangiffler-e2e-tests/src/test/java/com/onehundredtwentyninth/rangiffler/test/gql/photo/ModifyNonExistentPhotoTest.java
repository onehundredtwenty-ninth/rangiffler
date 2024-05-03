package com.onehundredtwentyninth.rangiffler.test.gql.photo;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlLikeInput;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlPhotoInput;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
@DisplayName("[gql] Photo")
class ModifyNonExistentPhotoTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private ObjectMapper mapper;

  @DisplayName("[gql] Обновление несуществующего фото")
  @ApiLogin
  @CreateUser
  @Test
  void updateNonExistentPhotoTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/updatePhoto.json") GqlRequest request) {
    var photoInput = mapper.convertValue(request.variables().get("input"), GqlPhotoInput.class);
    photoInput.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    request.variables().put("input", photoInput);

    var response = gatewayClient.updatePhoto(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasPhotoNotFoundMessage(UUID.fromString("00000000-0000-0000-0000-000000000000"))
              .hasPath(List.of("photo"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Проставление лайка несуществующему фото")
  @ApiLogin
  @CreateUser
  @Test
  void likeNonExistentPhotoTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/likePhoto.json") GqlRequest request) {
    var input = new GqlPhotoInput();
    input.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    input.setLike(new GqlLikeInput(user.getId()));
    request.variables().put("input", input);

    var response = gatewayClient.updatePhoto(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasPhotoNotFoundMessage(UUID.fromString("00000000-0000-0000-0000-000000000000"))
              .hasPath(List.of("photo"))
              .hasInternalErrorExtension();
        }
    );
  }

  @DisplayName("[gql] Удаление несуществующего фото")
  @ApiLogin
  @CreateUser
  @Test
  void deleteNonExistentPhotoTest(@Token String token, @GqlRequestFile("gql/deletePhoto.json") GqlRequest request) {
    request.variables().put("id", "00000000-0000-0000-0000-000000000000");
    var response = gatewayClient.deletePhoto(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions -> {
          softAssertions.assertThat(response)
              .hasErrorsCount(1);

          softAssertions.assertThat(response.getErrors().get(0))
              .hasPhotoNotFoundMessage(UUID.fromString("00000000-0000-0000-0000-000000000000"))
              .hasPath(List.of("deletePhoto"))
              .hasInternalErrorExtension();
        }
    );
  }
}
