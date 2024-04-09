package com.onehundredtwentyninth.rangiffler.test.grpc;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeoServiceTest {

  private static final Config CFG = Config.getInstance();
  private RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var uri = URI.create(CFG.geoUrl());
    var channel = ManagedChannelBuilder.forAddress(uri.getHost(), uri.getPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerGeoServiceGrpc.newBlockingStub(channel);
  }

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
