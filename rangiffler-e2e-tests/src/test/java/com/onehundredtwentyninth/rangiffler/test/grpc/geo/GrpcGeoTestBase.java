package com.onehundredtwentyninth.rangiffler.test.grpc.geo;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.grpc.ChannelProvider;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GrpcTest;
import org.junit.jupiter.api.BeforeEach;

@GrpcTest
public abstract class GrpcGeoTestBase {

  protected static final Config CFG = Config.getInstance();
  protected RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub blockingStub;

  @BeforeEach
  void before() {
    var channel = ChannelProvider.INSTANCE.channel(CFG.geoHost(), CFG.geoPort());
    blockingStub = RangifflerGeoServiceGrpc.newBlockingStub(channel);
  }
}
