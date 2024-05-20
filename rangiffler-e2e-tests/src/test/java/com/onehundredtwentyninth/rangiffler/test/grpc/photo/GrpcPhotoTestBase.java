package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.ChannelProvider;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import org.junit.jupiter.api.BeforeEach;

@GrpcTest
public abstract class GrpcPhotoTestBase {

  protected static final Config CFG = Config.getInstance();
  protected RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var channel = ChannelProvider.INSTANCE.channel(CFG.photoHost(), CFG.photoPort());
    blockingStub = RangifflerPhotoServiceGrpc.newBlockingStub(channel);
  }
}
