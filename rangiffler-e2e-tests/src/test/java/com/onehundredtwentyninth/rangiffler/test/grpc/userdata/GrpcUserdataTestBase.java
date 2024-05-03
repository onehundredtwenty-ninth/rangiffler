package com.onehundredtwentyninth.rangiffler.test.grpc.userdata;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.ChannelProvider;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import org.junit.jupiter.api.BeforeEach;

@GrpcTest
public abstract class GrpcUserdataTestBase {

  protected static final Config CFG = Config.getInstance();
  protected RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var channel = ChannelProvider.INSTANCE.channel(CFG.userdataHost(), CFG.userdataPort());
    blockingStub = RangifflerUserdataServiceGrpc.newBlockingStub(channel);
  }
}
