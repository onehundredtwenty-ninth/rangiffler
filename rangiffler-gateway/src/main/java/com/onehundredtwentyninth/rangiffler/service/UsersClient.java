package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.model.FriendStatus;
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

  public UserJson getUser(String userName) {
    var request = UserRequest.newBuilder().setUsername(userName).build();
    return UserJson.fromGrpcMessage(rangifflerUserdataServiceBlockingStub.getUser(request));
  }

  public @Nonnull Slice<UserJson> getFriends(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getUserFriends(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(s -> UserJson.friendFromGrpcMessage(s, FriendStatus.FRIEND))
        .toList();
    return new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext());
  }

  public @Nonnull Slice<UserJson> getFriendshipRequests(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getFriendshipRequests(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(s -> UserJson.friendFromGrpcMessage(s, FriendStatus.INVITATION_RECEIVED))
        .toList();
    return new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext());
  }

  public @Nonnull Slice<UserJson> getFriendshipAddresses(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getFriendshipAddresses(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(s -> UserJson.friendFromGrpcMessage(s, FriendStatus.INVITATION_SENT))
        .toList();
    return new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext());
  }
}
