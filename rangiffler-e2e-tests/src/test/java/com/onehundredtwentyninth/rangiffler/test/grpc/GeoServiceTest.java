package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.Assertions;
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
    Assertions.assertAll(
        () -> Assertions.assertNotNull(response),
        () -> Assertions.assertEquals(238, response.getAllCountriesCount()),
        () -> Assertions.assertNotNull(response.getAllCountries(0).getId()),
        () -> Assertions.assertNotNull(response.getAllCountries(0).getFlag()),
        () -> Assertions.assertNotNull(response.getAllCountries(0).getCode()),
        () -> Assertions.assertNotNull(response.getAllCountries(0).getName())
    );
  }
}
