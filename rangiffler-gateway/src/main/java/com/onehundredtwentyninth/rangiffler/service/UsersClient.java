package com.onehundredtwentyninth.rangiffler.service;

import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.grpc.UserByIdRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.model.FriendStatus;
import com.onehundredtwentyninth.rangiffler.model.UserInput;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import jakarta.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class UsersClient {

  @GrpcClient("grpcUserdataClient")
  private RangifflerUserdataServiceBlockingStub rangifflerUserdataServiceBlockingStub;
  @GrpcClient("grpcGeoClient")
  private RangifflerGeoServiceBlockingStub rangifflerGeoServiceBlockingStub;

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

  public UserJson getUserById(UUID id) {
    var request = UserByIdRequest.newBuilder().setId(id.toString()).build();
    return UserJson.fromGrpcMessage(rangifflerUserdataServiceBlockingStub.getUserById(request));
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

  public List<UUID> getUserFriendsIds(String userName) {
    var requestParameters = UserRequest.newBuilder().setUsername(userName).build();
    return rangifflerUserdataServiceBlockingStub.getUserFriendsIds(requestParameters)
        .getUserIdsList().stream()
        .map(UUID::fromString)
        .toList();
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

  public UserJson updateUser(String username, UserInput userInput) {
    var country = rangifflerGeoServiceBlockingStub.getCountryByCode(
        GetCountryByCodeRequest.newBuilder().setCode(userInput.location().code()).build()
    );
    var request = User.newBuilder()
        .setUsername(username)
        .setFirstname(userInput.firstname())
        .setLastName(userInput.surname())
        .setAvatar(ByteString.copyFrom(userInput.avatar().getBytes(StandardCharsets.UTF_8)))
        .setCountryId(country.getId())
        .build();

    var response = rangifflerUserdataServiceBlockingStub.updateUser(request);
    return UserJson.fromGrpcMessage(response);
  }
}
