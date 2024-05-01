package com.onehundredtwentyninth.rangiffler.test.gql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.model.UserInput;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.USERS)
@Feature(Features.USER)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.USER)})
class UpdateUserTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private UserRepository userRepository;
  @Inject
  private CountryRepository countryRepository;
  @Inject
  private ObjectMapper mapper;

  @DisplayName("Обновление пользователя")
  @ApiLogin
  @CreateUser
  @Test
  void updateUserTest(@Token String token, TestUser user, @GqlRequestFile("gql/updateUser.json") GqlRequest request) {
    var response = gatewayClient.updateUser(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    var userInput = mapper.convertValue(request.variables().get("input"), UserInput.class);
    var dbUser = userRepository.findById(user.getId());
    var country = countryRepository.findCountryByCode(userInput.getLocation().getCode());

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData().getUser())
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(userInput.getFirstname())
            .hasLastName(userInput.getSurname())
            .hasAvatar(userInput.getAvatar().getBytes(StandardCharsets.UTF_8))
            .hasCountryCode(userInput.getLocation().getCode())
    );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(dbUser)
            .hasId(user.getId())
            .hasUsername(user.getUsername())
            .hasFirstName(userInput.getFirstname())
            .hasLastName(userInput.getSurname())
            .hasAvatar(userInput.getAvatar().getBytes(StandardCharsets.UTF_8))
            .hasCountryId(country.getId())
    );
  }
}