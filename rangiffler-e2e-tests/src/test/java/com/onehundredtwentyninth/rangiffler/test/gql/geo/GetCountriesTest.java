package com.onehundredtwentyninth.rangiffler.test.gql.geo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.api.GatewayClient;
import com.onehundredtwentyninth.rangiffler.assertion.GqlSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlTest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GqlTest
@Epic(Epics.GEO)
@Feature(Features.COUNTRY_LIST)
@Tags({@Tag(Layers.GQL), @Tag(Suites.SMOKE), @Tag(JUnitTags.GEO), @Tag(JUnitTags.COUNTRY_LIST)})
@DisplayName("[gql] Geo")
class GetCountriesTest {

  @Inject
  private GatewayClient gatewayClient;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("[gql] Получение списка всех стран")
  @ApiLogin
  @CreateUser
  @Test
  void getCountriesTest(@Token String token, @GqlRequestFile("gql/getCountries.json") GqlRequest request) {
    var response = gatewayClient.getCountries(token, request);

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasNotErrors()
            .dataNotNull()
    );

    var countriesCount = countryRepository.count();
    var expectedCountryDb = countryRepository.findCountryByCode("ru");
    var expectedCountry = new GqlCountry(expectedCountryDb.getCode(), expectedCountryDb.getName(),
        new String(expectedCountryDb.getFlag()));

    GqlSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getData())
            .countriesNotNull()
            .hasCountriesCount(countriesCount)
            .containsCountry(expectedCountry)
    );
  }
}
