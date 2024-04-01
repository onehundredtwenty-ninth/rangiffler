package com.onehundredtwentyninth.service;

import com.onehundredtwentyninth.data.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersRequest;
import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import io.grpc.stub.StreamObserver;
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
                    .build()
            ).toList()
        )
        .setHasNext(allUsersEntities.getTotalPages() > request.getPage() + 1)
        .build();

    responseObserver.onNext(allUsersResponse);
    responseObserver.onCompleted();
  }
}
