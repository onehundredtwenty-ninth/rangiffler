package com.onehundredtwentyninth.rangiffler.client;

import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.FriendshipAction;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc.RangifflerUserdataServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.grpc.UpdateUserFriendshipRequest;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.grpc.UserByIdRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.model.FriendshipInput;
import com.onehundredtwentyninth.rangiffler.model.GqlUser;
import com.onehundredtwentyninth.rangiffler.model.UserInput;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class UsersClient {

  @GrpcClient("grpcUserdataClient")
  private RangifflerUserdataServiceBlockingStub rangifflerUserdataServiceBlockingStub;
  @Autowired
  private GeoClient geoClient;

  public Slice<GqlUser> getAllUsers(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getAllUsers(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(GqlUser::fromGrpcMessage)
        .toList();
    return !users.isEmpty()
        ? new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext())
        : null;
  }

  public GqlUser getUser(String userName) {
    var request = UserRequest.newBuilder().setUsername(userName).build();
    return GqlUser.fromGrpcMessage(rangifflerUserdataServiceBlockingStub.getUser(request));
  }

  public GqlUser getUserById(UUID id) {
    var request = UserByIdRequest.newBuilder().setId(id.toString()).build();
    return GqlUser.fromGrpcMessage(rangifflerUserdataServiceBlockingStub.getUserById(request));
  }

  public Slice<GqlUser> getFriends(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getUserFriends(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(GqlUser::fromGrpcMessage)
        .toList();
    return !users.isEmpty()
        ? new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext())
        : null;
  }

  public List<UUID> getUserFriendsIds(String userName) {
    var requestParameters = UserRequest.newBuilder().setUsername(userName).build();
    return rangifflerUserdataServiceBlockingStub.getUserFriendsIds(requestParameters)
        .getUserIdsList().stream()
        .map(UUID::fromString)
        .toList();
  }

  public Slice<GqlUser> getFriendshipRequests(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getFriendshipRequests(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(GqlUser::fromGrpcMessage)
        .toList();
    return !users.isEmpty()
        ? new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext())
        : null;
  }

  public Slice<GqlUser> getFriendshipAddresses(String username, int page, int size, String searchQuery) {
    var requestParameters = AllUsersRequest.newBuilder()
        .setUsername(username)
        .setPage(page)
        .setSize(size)
        .setSearchQuery(searchQuery)
        .build();
    var response = rangifflerUserdataServiceBlockingStub.getFriendshipAddresses(requestParameters);

    var users = response.getAllUsersList()
        .stream()
        .map(GqlUser::fromGrpcMessage)
        .toList();
    return !users.isEmpty()
        ? new SliceImpl<>(users, PageRequest.of(page, size), response.getHasNext())
        : null;
  }

  public GqlUser updateUser(String username, UserInput userInput) {
    var country = geoClient.getCountryByCode(userInput.location().code());
    var request = User.newBuilder()
        .setUsername(username)
        .setFirstname(userInput.firstname())
        .setLastName(userInput.surname())
        .setAvatar(ByteString.copyFrom(userInput.avatar().getBytes(StandardCharsets.UTF_8)))
        .setCountryId(country.id().toString())
        .build();

    var response = rangifflerUserdataServiceBlockingStub.updateUser(request);
    return GqlUser.fromGrpcMessage(response);
  }

  public GqlUser updateFriendshipStatus(String username, FriendshipInput friendshipInput) {
    var actionAuthorUser = getUser(username);
    var request = UpdateUserFriendshipRequest.newBuilder()
        .setActionAuthorUserId(actionAuthorUser.id().toString())
        .setActionTargetUserId(friendshipInput.user().toString())
        .setAction(FriendshipAction.valueOf(friendshipInput.action().name()))
        .build();

    var response = rangifflerUserdataServiceBlockingStub.updateUserFriendship(request);
    return GqlUser.fromGrpcMessage(response);
  }
}
