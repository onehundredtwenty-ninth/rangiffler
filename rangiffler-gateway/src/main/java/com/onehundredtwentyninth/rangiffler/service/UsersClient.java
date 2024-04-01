package com.onehundredtwentyninth.rangiffler.service;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import jakarta.annotation.Nonnull;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class UsersClient {

  private static final Empty EMPTY = Empty.getDefaultInstance();
  @GrpcClient("grpcUserdataClient")
  private RangifflerUserdataServiceBlockingStub rangifflerUserdataServiceBlockingStub;

  public @Nonnull List<UserJson> getAllUsers() {
    return rangifflerUserdataServiceBlockingStub.getAllUsers(EMPTY).getAllUsersList()
        .stream()
        .map(UserJson::fromGrpcMessage)
        .toList();
  }
}
