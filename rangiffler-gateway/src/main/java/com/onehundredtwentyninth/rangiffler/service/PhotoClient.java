package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.grpc.PhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.model.PhotoJson;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class PhotoClient {

  @GrpcClient("grpcPhotoClient")
  private RangifflerPhotoServiceBlockingStub rangifflerPhotoServiceBlockingStub;
  @Autowired
  private UsersClient usersClient;

  public @Nonnull Slice<PhotoJson> getPhotos(String userName, int page, int size) {
    var user = usersClient.getUser(userName);
    var response = rangifflerPhotoServiceBlockingStub.getPhotos(
        PhotoRequest.newBuilder()
            .setUserId(user.id().toString())
            .setPage(page)
            .setSize(size)
            .build()
    );

    var photos = response.getPhotosList()
        .stream()
        .map(PhotoJson::fromGrpcMessage)
        .toList();
    return new SliceImpl<>(photos, PageRequest.of(page, size), response.getHasNext());
  }
}
