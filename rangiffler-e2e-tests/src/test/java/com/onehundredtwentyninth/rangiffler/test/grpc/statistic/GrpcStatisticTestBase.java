package com.onehundredtwentyninth.rangiffler.test.grpc.statistic;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.ChannelProvider;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerStatisticServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import org.junit.jupiter.api.BeforeEach;

@GrpcTest
public abstract class GrpcStatisticTestBase {

  protected static final Config CFG = Config.getInstance();
  protected RangifflerStatisticServiceGrpc.RangifflerStatisticServiceBlockingStub blockingStub;
  protected RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub photoServiceBlockingStub;

  @BeforeEach
  void before() {
    var channel = ChannelProvider.INSTANCE.channel(CFG.photoHost(), CFG.photoPort());
    blockingStub = RangifflerStatisticServiceGrpc.newBlockingStub(channel);
    photoServiceBlockingStub = RangifflerPhotoServiceGrpc.newBlockingStub(channel);
  }
}
