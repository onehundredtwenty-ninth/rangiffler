package com.onehundredtwentyninth.service;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.data.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import io.grpc.stub.StreamObserver;
import java.util.stream.Collectors;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserDataService extends RangifflerUserdataServiceGrpc.RangifflerUserdataServiceImplBase {

  private final UserRepository userRepository;

  @Autowired
  public UserDataService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void getAllUsers(Empty request, StreamObserver<AllUsersResponse> responseObserver) {
    var allUsersEntities = userRepository.findAll();

    var allUsersResponse = AllUsersResponse.newBuilder().addAllAllUsers(
            allUsersEntities.stream().map(
                entity -> User.newBuilder()
                    .setId(entity.getId().toString())
                    .setUsername(entity.getUsername())
                    .setFirstname(entity.getFirstname())
                    .setLastName(entity.getLastName())
                    .build()
            ).collect(Collectors.toList())
        )
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }
}
