package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.data.FriendshipEntity;
import com.onehundredtwentyninth.rangiffler.data.FriendshipStatus;
import com.onehundredtwentyninth.rangiffler.data.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.data.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.exception.FriendshipNotFoundException;
import com.onehundredtwentyninth.rangiffler.exception.FriendshipRequestNotFoundException;
import com.onehundredtwentyninth.rangiffler.exception.UserNotFoundException;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.UpdateUserFriendshipRequest;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.grpc.UserByIdRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UserIdsResponse;
import com.onehundredtwentyninth.rangiffler.grpc.UserRequest;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import io.grpc.stub.StreamObserver;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class UserDataService extends RangifflerUserdataServiceGrpc.RangifflerUserdataServiceImplBase {

  private final UserRepository userRepository;
  private final FriendshipRepository friendshipRepository;

  @Autowired
  public UserDataService(UserRepository userRepository, FriendshipRepository friendshipRepository) {
    this.userRepository = userRepository;
    this.friendshipRepository = friendshipRepository;
  }

  @Override
  public void getAllUsers(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var allUsersEntities = userRepository.findByUsernameNotAndSearchQuery(request.getUsername(),
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(UserEntityMapper::toMessage).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUser(UserRequest request, StreamObserver<User> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    var userResponse = UserEntityMapper.toMessage(userEntity);

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserById(UserByIdRequest request, StreamObserver<User> responseObserver) {
    var userEntity = userRepository.findById(UUID.fromString(request.getId()))
        .orElseThrow(() -> new UserNotFoundException(request.getId()));
    var userResponse = UserEntityMapper.toMessage(userEntity);

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserFriends(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    var allUsersEntities = userRepository.findFriends(userEntity,
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(UserEntityMapper::toMessage).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserFriendsIds(UserRequest request, StreamObserver<UserIdsResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    var userFriendIds = userRepository.findFriendsIds(userEntity);

    var idsResponse = UserIdsResponse.newBuilder()
        .addAllUserIds(userFriendIds.stream().map(UUID::toString).toList())
        .build();

    responseObserver.onNext(idsResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getFriendshipRequests(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    var allUsersEntities = userRepository.findIncomeInvitations(userEntity,
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(UserEntityMapper::toMessage).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getFriendshipAddresses(AllUsersRequest request, StreamObserver<AllUsersResponse> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    var allUsersEntities = userRepository.findOutcomeInvitations(userEntity,
        PageRequest.of(request.getPage(), request.getSize()),
        request.getSearchQuery()
    );

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(UserEntityMapper::toMessage).toList()
        )
        .setHasNext(allUsersEntities.hasNext())
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void updateUser(User request, StreamObserver<User> responseObserver) {
    var userEntity = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    userEntity.setFirstname(request.getFirstname());
    userEntity.setLastName(request.getLastName());
    userEntity.setAvatar(request.getAvatar().toByteArray());
    userEntity.setCountryId(request.getCountryId());
    userRepository.save(userEntity);

    var userResponse = UserEntityMapper.toMessage(userEntity);

    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void updateUserFriendship(UpdateUserFriendshipRequest request, StreamObserver<User> responseObserver) {
    var actionAuthorUser = userRepository.findById(UUID.fromString(request.getActionAuthorUserId()))
        .orElseThrow(() -> new UserNotFoundException(request.getActionAuthorUserId()));
    var actionTargetUser = userRepository.findById(UUID.fromString(request.getActionTargetUserId()))
        .orElseThrow(() -> new UserNotFoundException(request.getActionTargetUserId()));

    var action = request.getAction();
    switch (action) {
      case ADD -> {
        var friendshipEntity = new FriendshipEntity();
        friendshipEntity.setRequester(actionAuthorUser);
        friendshipEntity.setAddressee(actionTargetUser);
        friendshipEntity.setStatus(FriendshipStatus.PENDING);
        friendshipEntity.setCreatedDate(Timestamp.from(Instant.now()));
        friendshipRepository.save(friendshipEntity);
      }
      case DELETE -> {
        var friendshipEntity = friendshipRepository.findFriendship(actionAuthorUser, actionTargetUser).orElseThrow(
            () -> new FriendshipNotFoundException(actionAuthorUser.getUsername(), actionTargetUser.getUsername())
        );
        friendshipRepository.delete(friendshipEntity);
      }
      case ACCEPT -> {
        var friendshipEntity = friendshipRepository.findByRequesterAndAddressee(actionTargetUser, actionAuthorUser)
            .orElseThrow(() -> new FriendshipRequestNotFoundException(
                    actionTargetUser.getUsername(), actionAuthorUser.getUsername()
                )
            );
        friendshipEntity.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendshipEntity);
      }
      case REJECT -> {
        var friendshipEntity = friendshipRepository.findByRequesterAndAddressee(actionTargetUser, actionAuthorUser)
            .orElseThrow(() -> new FriendshipRequestNotFoundException(
                    actionTargetUser.getUsername(), actionAuthorUser.getUsername()
                )
            );
        friendshipRepository.delete(friendshipEntity);
      }
      default -> throw new IllegalStateException();
    }

    var userResponse = UserEntityMapper.toMessage(actionAuthorUser);
    responseObserver.onNext(userResponse);
    responseObserver.onCompleted();
  }
}
