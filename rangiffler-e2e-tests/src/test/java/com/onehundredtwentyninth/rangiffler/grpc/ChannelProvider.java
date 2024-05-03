package com.onehundredtwentyninth.rangiffler.grpc;

import com.onehundredtwentyninth.rangiffler.grpc.interceptor.GrpcConsoleWithoutByteStringInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ChannelProvider {

  INSTANCE;
  private final Map<String, Channel> store = new ConcurrentHashMap<>();

  public synchronized Channel channel(String name, int port) {
    return store.computeIfAbsent(name + port, k ->
        ManagedChannelBuilder.forAddress(name, port)
            .intercept(new AllureGrpc(), new GrpcConsoleWithoutByteStringInterceptor())
            .usePlaintext()
            .build()
    );
  }

  public void closeAllChannels() {
    log.info("Grpc channels stat\nChannels count: {}\nChannels names: {}", store.size(), store.keySet());
    log.info("Close all grpc channels");
    store.values().forEach(s -> ((ManagedChannel) s).shutdownNow());
    log.info("Grpc channels successfully closed");
  }
}
