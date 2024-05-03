package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.interceptor.GrpcConsoleWithoutByteStringInterceptor;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@GrpcTest
public abstract class GrpcPhotoTestBase {

  protected static final Config CFG = Config.getInstance();
  protected static RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub blockingStub;

  @BeforeAll
  static void before() {
    var channel = ManagedChannelBuilder.forAddress(CFG.photoHost(), CFG.photoPort())
        .intercept(new AllureGrpc(), new GrpcConsoleWithoutByteStringInterceptor())
        .usePlaintext()
        .build();
    blockingStub = RangifflerPhotoServiceGrpc.newBlockingStub(channel);
  }

  @AfterAll
  static void after() {
    ((ManagedChannel) blockingStub.getChannel()).shutdownNow();
  }
}
