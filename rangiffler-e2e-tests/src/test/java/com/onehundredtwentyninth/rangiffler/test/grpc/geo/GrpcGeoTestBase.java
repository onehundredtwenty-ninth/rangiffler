package com.onehundredtwentyninth.rangiffler.test.grpc.geo;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@GrpcTest
public abstract class GrpcGeoTestBase {

  protected static final Config CFG = Config.getInstance();
  protected RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.geoHost(), CFG.geoPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerGeoServiceGrpc.newBlockingStub(channel);
  }

  @AfterEach
  void after() {
    ((ManagedChannel) blockingStub.getChannel()).shutdownNow();
  }
}
