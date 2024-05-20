package com.onehundredtwentyninth.rangiffler.test.gql.userdata;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER_FRIENDSHIP)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER_FRIENDSHIP)})
@DisplayName("[gql] Userdata")
class GetPeopleTest {

  @Inject
  private GatewayClient gatewayClient;

  @DisplayName("[gql] Получение всех пользователей")
  @ApiLogin
  @CreateUser
  @Test
  void getPeopleTest(@Token String token, @GqlRequestFile("gql/getPeople.json") GqlRequest request) {
    var response = gatewayClient.getPeople(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers())
            .hasEdgesCount(10)
            .hasPrevious(false)
            .hasNext(true)
    );
  }

  @DisplayName("[gql] Получение пользователей при передаче SearchQuery username автора запроса")
  @ApiLogin
  @CreateUser
  @Test
  void getPeoplePageByPageTest(@Token String token, TestUser user,
      @GqlRequestFile("gql/getPeople.json") GqlRequest request) {
    request.variables().put("searchQuery", user.getUsername());
    var response = gatewayClient.getPeople(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    Assertions.assertThat(response.getData().getUsers())
        .isNull();
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[gql] Получение всех пользователей по переданному username")
  @ApiLogin
  @CreateUser
  @Test
  void getPeopleWithUsernameFilterTest(@Token String token,
      @GqlRequestFile("gql/getPeople.json") GqlRequest request, @Extras TestUser[] users) {
    request.variables().put("searchQuery", users[0].getUsername());
    var response = gatewayClient.getPeople(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedUser = users[0];
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers().getEdges().get(0))
            .hasId(expectedUser.getId())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
    );
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[gql] Получение всех пользователей по переданному firstname")
  @ApiLogin
  @CreateUser
  @Test
  void getPeopleWithFirstnameFilterTest(@Token String token,
      @GqlRequestFile("gql/getPeople.json") GqlRequest request, @Extras TestUser[] users) {
    request.variables().put("searchQuery", users[0].getFirstname());
    var response = gatewayClient.getPeople(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedUser = users[0];
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers().getEdges().get(0))
            .hasId(expectedUser.getId())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
    );
  }

  @CreateExtrasUsers(@CreateUser)
  @DisplayName("[gql] Получение всех пользователей по переданному surname")
  @ApiLogin
  @CreateUser
  @Test
  void getPeopleWithSurnameFilterTest(@Token String token,
      @GqlRequestFile("gql/getPeople.json") GqlRequest request, @Extras TestUser[] users) {
    request.variables().put("searchQuery", users[0].getLastName());
    var response = gatewayClient.getPeople(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers())
            .hasEdgesCount(1)
            .hasPrevious(false)
            .hasNext(false)
    );

    var expectedUser = users[0];
    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUsers().getEdges().get(0))
            .hasId(expectedUser.getId())
            .hasUsername(expectedUser.getUsername())
            .hasFirstName(expectedUser.getFirstname())
            .hasLastName(expectedUser.getLastName())
            .hasAvatar(expectedUser.getAvatar())
    );
  }
}
