package com.onehundredtwentyninth.rangiffler.test.gql;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.assertion.GqlUserAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Friend;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
class GetFriendsTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Получение друзей текущего пользователя")
  @ApiLogin
  @CreateUser(
      friends = {
          @Friend,
          @Friend
      }
  )
  @Test
  void getFriendsTest(@Token String token, TestUser user, @GqlRequestFile("gql/getFriends.json") GqlRequest request) {
    var response = gatewayClient.getCurrentUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlUserAssertions.assertThat(response.getData().getUser())
        .hasFriendsCount(user.getFriends().size())
        .hasPrevious(response.getData().getUser().getFriends().getPageInfo(), false)
        .hasNext(response.getData().getUser().getFriends().getPageInfo(), false);

    var expectedFriend = user.getFriends().get(0);
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser().getFriends().getEdges().get(0))
            .hasId(expectedFriend.getId())
            .hasUsername(expectedFriend.getUsername())
            .hasFirstName(expectedFriend.getFirstname())
            .hasLastName(expectedFriend.getLastName())
            .hasAvatar(expectedFriend.getAvatar())
    );
  }
}
