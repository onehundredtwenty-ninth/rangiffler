package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.google.inject.Inject;
import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.assertion.GrpcResponseSoftAssertions;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GrpcTest
@Epic(Epics.GEO)
@Feature(Features.COUNTRY_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.GEO), @Tag(Features.COUNTRY_LIST)})
class GeoServiceTest {

  @Inject
  private CountryRepository countryRepository;
  private static final Config CFG = Config.getInstance();
  private RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.geoHost(), CFG.geoPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerGeoServiceGrpc.newBlockingStub(channel);
  }

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
