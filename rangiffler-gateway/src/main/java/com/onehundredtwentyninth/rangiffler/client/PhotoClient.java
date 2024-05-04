package com.onehundredtwentyninth.rangiffler.client;

import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.grpc.CreatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.LikePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.model.GqlPhoto;
import com.onehundredtwentyninth.rangiffler.model.PhotoInput;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
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
  @Autowired
  private GeoClient geoClient;

  public Slice<GqlPhoto> getPhotos(String userName, int page, int size, boolean withFriends) {
    var userIds = new ArrayList<UUID>();
    userIds.add(usersClient.getUser(userName).id());

    if (withFriends) {
      userIds.addAll(usersClient.getUserFriendsIds(userName));
    }

    var response = rangifflerPhotoServiceBlockingStub.getPhotos(
        PhotoRequest.newBuilder()
            .addAllUserIds(userIds.stream().map(UUID::toString).toList())
            .setPage(page)
            .setSize(size)
            .build()
    );

    var photos = response.getPhotosList()
        .stream()
        .map(GqlPhoto::fromGrpcMessage)
        .toList();
    return !photos.isEmpty()
        ? new SliceImpl<>(photos, PageRequest.of(page, size), response.getHasNext())
        : null;
  }

  public GqlPhoto photo(String userName, PhotoInput photo) {
    if (photo.id() == null) {
      return createPhoto(userName, photo);
    }

    if (photo.like() != null) {
      return likePhoto(photo);
    }

    return updatePhoto(userName, photo);
  }

  public GqlPhoto createPhoto(String userName, PhotoInput photo) {
    var country = geoClient.getCountryByCode(photo.country().code()).id().toString();
    var user = usersClient.getUser(userName);

    var request = CreatePhotoRequest.newBuilder()
        .setUserId(user.id().toString())
        .setSrc(ByteString.copyFrom(photo.src() != null ? photo.src().getBytes(StandardCharsets.UTF_8) : new byte[]{}))
        .setCountryId(country)
        .setDescription(photo.description())
        .build();

    var response = rangifflerPhotoServiceBlockingStub.createPhoto(request);
    return GqlPhoto.fromGrpcMessage(response);
  }

  public GqlPhoto updatePhoto(String userName, PhotoInput photo) {
    var country = geoClient.getCountryByCode(photo.country().code()).id().toString();
    var user = usersClient.getUser(userName);

    var request = UpdatePhotoRequest.newBuilder()
        .setId(photo.id().toString())
        .setUserId(user.id().toString())
        .setCountryId(country)
        .setDescription(photo.description())
        .build();

    var response = rangifflerPhotoServiceBlockingStub.updatePhoto(request);
    return GqlPhoto.fromGrpcMessage(response);
  }

  public GqlPhoto likePhoto(PhotoInput photo) {
    var request = LikePhotoRequest.newBuilder()
        .setPhotoId(photo.id().toString())
        .setUserId(photo.like().user().toString())
        .build();

    var response = rangifflerPhotoServiceBlockingStub.likePhoto(request);
    return GqlPhoto.fromGrpcMessage(response);
  }

  public boolean deletePhoto(String userName, UUID photoId) {
    var user = usersClient.getUser(userName);
    var request = DeletePhotoRequest.newBuilder()
        .setPhotoId(photoId.toString())
        .setUserId(user.id().toString())
        .build();

    var response = rangifflerPhotoServiceBlockingStub.deletePhoto(request);
    return response.getValue();
  }
}
