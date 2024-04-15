package com.onehundredtwentyninth.rangiffler.test.grpc.geo;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.grpc.Country;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.GEO)
@Feature(Features.COUNTRY)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.GEO), @Tag(Features.COUNTRY)})
class GetCountryTest extends GrpcGeoTestBase {

  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Получение страны по коду")
  @Test
  void getCountryByCodeTest() {
    final Country response = blockingStub.getCountryByCode(
        GetCountryByCodeRequest.newBuilder()
            .setCode("us")
            .build()
    );
    final CountryEntity expectedCountry = countryRepository.findCountryByCode("us");

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedCountry.getId())
            .hasCode(expectedCountry.getCode())
            .hasName(expectedCountry.getName())
            .hasFlag(expectedCountry.getFlag())
    );
  }

  @DisplayName("Получение страны по id")
  @Test
  void getCountryByIdTest() {
    final CountryEntity expectedCountry = countryRepository.findCountryByCode("ru");
    final Country response = blockingStub.getCountry(
        GetCountryRequest.newBuilder()
            .setId(expectedCountry.getId().toString())
            .build()
    );

    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response)
            .hasId(expectedCountry.getId())
            .hasCode(expectedCountry.getCode())
            .hasName(expectedCountry.getName())
            .hasFlag(expectedCountry.getFlag())
    );
  }
}
