package com.onehundredtwentyninth.rangiffler.test.grpc.statistic;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerStatisticServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.GrpcTest;
import com.onehundredtwentyninth.rangiffler.utils.GrpcConsoleInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@GrpcTest
public abstract class GrpcStatisticTestBase {

  protected static final Config CFG = Config.getInstance();
  protected RangifflerStatisticServiceGrpc.RangifflerStatisticServiceBlockingStub blockingStub;
  protected RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub photoServiceBlockingStub;

  @BeforeEach
  void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.photoHost(), CFG.photoPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerStatisticServiceGrpc.newBlockingStub(channel);
    photoServiceBlockingStub = RangifflerPhotoServiceGrpc.newBlockingStub(channel);
  }

  @AfterEach
  void after() {
    ((ManagedChannel) blockingStub.getChannel()).shutdownNow();
  }
}
