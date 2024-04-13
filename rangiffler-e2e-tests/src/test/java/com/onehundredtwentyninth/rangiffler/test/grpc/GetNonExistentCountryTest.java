package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.grpc.AllureGrpc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@GrpcTest
@Epic(Epics.GEO)
@Feature(Features.COUNTRY)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(Epics.GEO), @Tag(Features.COUNTRY)})
class GetNonExistentCountryTest {

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

  @DisplayName("Получение страны по несуществующему коду")
  @Test
  void getCountryByNonExistentCodeTest() {
    var request = GetCountryByCodeRequest.newBuilder().setCode("nonExistentCode").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getCountryByCode(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: Country nonExistentCode not found");
  }

  @DisplayName("Получение страны по несуществующему id")
  @Test
  void getCountryByNonExistentIdTest() {
    var request = GetCountryRequest.newBuilder().setId("00000000-0000-0000-0000-000000000000").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getCountry(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: Country 00000000-0000-0000-0000-000000000000 not found");
  }

  @DisplayName("Получение страны по невалидному id")
  @Test
  void getCountryByInvalidIdTest() {
    var request = GetCountryRequest.newBuilder().setId("notValidId").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getCountry(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("ABORTED: Invalid UUID string: notValidId");
  }
}
