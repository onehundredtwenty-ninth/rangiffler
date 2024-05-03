package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.onehundredtwentyninth.rangiffler.grpc.ChannelProvider;

public class GrpcChannelExtension implements SuiteExtension {

  @Override
  public void afterSuite() {
    ChannelProvider.INSTANCE.closeAllChannels();
  }
}
