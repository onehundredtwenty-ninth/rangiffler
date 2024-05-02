package com.onehundredtwentyninth.rangiffler.test.grpc.statistic;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerStatisticServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@GrpcTest
public abstract class GrpcStatisticTestBase {

  protected static final Config CFG = Config.getInstance();
  protected static RangifflerStatisticServiceGrpc.RangifflerStatisticServiceBlockingStub blockingStub;
  protected static RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub photoServiceBlockingStub;

  @BeforeAll
  static void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.photoHost(), CFG.photoPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerStatisticServiceGrpc.newBlockingStub(channel);
    photoServiceBlockingStub = RangifflerPhotoServiceGrpc.newBlockingStub(channel);
  }

  @AfterAll
  static void after() {
    ((ManagedChannel) blockingStub.getChannel()).shutdownNow();
  }
}
