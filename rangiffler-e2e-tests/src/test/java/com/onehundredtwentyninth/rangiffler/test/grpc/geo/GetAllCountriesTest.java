package com.onehundredtwentyninth.rangiffler.test.grpc.geo;

import com.google.inject.Inject;
import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.GEO)
@Feature(Features.COUNTRY_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.GEO), @Tag(Features.COUNTRY_LIST)})
class GetAllCountriesTest extends GrpcGeoTestBase {

  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Получение списка всех стран")
  @Test
  void allCountriesTest() {
    final AllCountriesResponse response = blockingStub.getAllCountries(Empty.getDefaultInstance());

    var count = countryRepository.count();
    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response)
          .describedAs("Ответ сервиса не null")
          .isNotNull();

      softAssertions.assertThat(response.getAllCountriesCount())
          .describedAs("Количество в списке стран совпадает с количеством в БД")
          .isEqualTo(count);

    });

    var expectedCountry = countryRepository.findCountryByCode(response.getAllCountries(0).getCode());
    GrpcResponseSoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(response.getAllCountries(0))
            .hasId(expectedCountry.getId())
            .hasCode(expectedCountry.getCode())
            .hasName(expectedCountry.getName())
            .hasFlag(expectedCountry.getFlag())
    );
  }
}
