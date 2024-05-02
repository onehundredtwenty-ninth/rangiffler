package com.onehundredtwentyninth.rangiffler.test.grpc.geo;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@GrpcTest
public abstract class GrpcGeoTestBase {

  protected static final Config CFG = Config.getInstance();
  protected static RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub blockingStub;

  @BeforeAll
  static void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.geoHost(), CFG.geoPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerGeoServiceGrpc.newBlockingStub(channel);
  }

  @AfterAll
  static void after() {
    ((ManagedChannel) blockingStub.getChannel()).shutdownNow();
  }
}
