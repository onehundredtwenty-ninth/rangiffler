package com.onehundredtwentyninth.service;

import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.data.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.grpc.UserByIdRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UserIdsResponse;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@GrpcService
public class UserDataService extends RangifflerUserdataServiceGrpc.RangifflerUserdataServiceImplBase {

  private final UserRepository userRepository;

  @Autowired
  public UserDataService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void getAllUsers(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var allUsersEntities = userRepository.findByUsernameNotAndSearchQuery(request.getUsername(),
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(
                entity -> User.newBuilder()
                    .setId(entity.getId().toString())
                    .setUsername(entity.getUsername())
                    .setFirstname(entity.getFirstname())
                    .setLastName(entity.getLastName())
                    .setAvatar(ByteString.copyFrom(entity.getAvatar() != null ? entity.getAvatar() : new byte[]{}))
                    .setCountryId(entity.getCountryId())
                    .build()
            ).toList()
        )
        .setHasNext(allUsersEntities.getTotalPages() > request.getPage() + 1)
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUser(UserRequest request, StreamObserver<User> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();

    var userResponse = User.newBuilder()
        .setId(userEntity.getId().toString())
        .setUsername(userEntity.getUsername())
        .setFirstname(userEntity.getFirstname())
        .setLastName(userEntity.getLastName())
        .setAvatar(ByteString.copyFrom(userEntity.getAvatar() != null ? userEntity.getAvatar() : new byte[]{}))
        .setCountryId(userEntity.getCountryId())
        .build();

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserById(UserByIdRequest request, StreamObserver<User> responseObserver) {
    var userEntity = userRepository.findById(UUID.fromString(request.getId())).orElseThrow();

    var userResponse = User.newBuilder()
        .setId(userEntity.getId().toString())
        .setUsername(userEntity.getUsername())
        .setFirstname(userEntity.getFirstname())
        .setLastName(userEntity.getLastName())
        .setAvatar(ByteString.copyFrom(userEntity.getAvatar() != null ? userEntity.getAvatar() : new byte[]{}))
        .setCountryId(userEntity.getCountryId())
        .build();

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserFriends(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
    var allUsersEntities = userRepository.findFriends(userEntity,
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(
                entity -> User.newBuilder()
                    .setId(entity.getId().toString())
                    .setUsername(entity.getUsername())
                    .setFirstname(entity.getFirstname())
                    .setLastName(entity.getLastName())
                    .setAvatar(ByteString.copyFrom(entity.getAvatar() != null ? entity.getAvatar() : new byte[]{}))
                    .setCountryId(entity.getCountryId())
                    .build()
            ).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserFriendsIds(UserRequest request, StreamObserver<UserIdsResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
    var userFriendIds = userRepository.findFriendsIds(userEntity);

    var idsResponse = UserIdsResponse.newBuilder()
        .addAllUserIds(userFriendIds.stream().map(UUID::toString).toList())
        .build();

    responseObserver.onNext(idsResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getFriendshipRequests(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
    var allUsersEntities = userRepository.findIncomeInvitations(userEntity,
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(
                entity -> User.newBuilder()
                    .setId(entity.getId().toString())
                    .setUsername(entity.getUsername())
                    .setFirstname(entity.getFirstname())
                    .setLastName(entity.getLastName())
                    .setAvatar(ByteString.copyFrom(entity.getAvatar() != null ? entity.getAvatar() : new byte[]{}))
                    .setCountryId(entity.getCountryId())
                    .build()
            ).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getFriendshipAddresses(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow();
    var allUsersEntities = userRepository.findOutcomeInvitations(userEntity,
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(
                entity -> User.newBuilder()
                    .setId(entity.getId().toString())
                    .setUsername(entity.getUsername())
                    .setFirstname(entity.getFirstname())
                    .setLastName(entity.getLastName())
                    .setAvatar(ByteString.copyFrom(entity.getAvatar() != null ? entity.getAvatar() : new byte[]{}))
                    .setCountryId(entity.getCountryId())
                    .build()
            ).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }
}
