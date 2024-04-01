package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class UsersClient {

  @GrpcClient("grpcUserdataClient")
  private RangifflerUserdataServiceBlockingStub rangifflerUserdataServiceBlockingStub;

  public @Nonnull Slice<UserJson> getAllUsers(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getAllUsers(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(UserJson::fromGrpcMessage)
        .toList();
    return new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext());
  }
}
