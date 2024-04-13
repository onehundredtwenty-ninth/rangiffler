package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.GEO)
@Feature(Features.COUNTRY_LIST)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.GEO), @Tag(Features.COUNTRY_LIST)})
class GeoServiceTest {

  private static final Config CFG = Config.getInstance();
  private RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub blockingStub;
  private final CountryRepository countryRepository = new CountryRepositorySJdbc();

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
    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response.getAllCountries(0).getId())
          .describedAs("Id страны из ответа сервиса совпадает с БД")
          .isEqualTo(expectedCountry.getId().toString());

      softAssertions.assertThat(response.getAllCountries(0).getFlag().getBytes(StandardCharsets.UTF_8))
          .describedAs("Флаг страны из ответа сервиса совпадает с БД")
          .isEqualTo(expectedCountry.getFlag());

      softAssertions.assertThat(response.getAllCountries(0).getCode())
          .describedAs("Код страны из ответа сервиса совпадает с БД")
          .isEqualTo(expectedCountry.getCode());

      softAssertions.assertThat(response.getAllCountries(0).getName())
          .describedAs("Имя страны из ответа сервиса совпадает с БД")
          .isEqualTo(expectedCountry.getName());
    });
  }
}
