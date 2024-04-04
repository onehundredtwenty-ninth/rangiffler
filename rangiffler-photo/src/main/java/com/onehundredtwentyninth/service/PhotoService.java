package com.onehundredtwentyninth.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.onehundredtwentyninth.data.PhotoEntity;
import com.onehundredtwentyninth.data.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.Like;
import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@GrpcService
public class PhotoService extends RangifflerPhotoServiceGrpc.RangifflerPhotoServiceImplBase {

  private final PhotoRepository photoRepository;

  @Autowired
  public PhotoService(PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
  }

  @Override
  public void getPhotos(PhotoRequest request, StreamObserver<PhotoResponse> responseObserver) {
    var photos = photoRepository.findByUserIdIn(
        request.getUserIdsList().stream().map(UUID::fromString).toList(),
        PageRequest.of(request.getPage(), request.getSize())
    );

    var photoResponse = buildPhotoResponseFromEntities(photos);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  private PhotoResponse buildPhotoResponseFromEntities(Page<PhotoEntity> photos) {
    return PhotoResponse.newBuilder()
        .addAllPhotos(
            photos.map(photoEntity -> Photo.newBuilder()
                .setId(photoEntity.getId().toString())
                .setSrc(ByteString.copyFrom(photoEntity.getPhoto() != null ? photoEntity.getPhoto() : new byte[]{}))
                .setCountryId(photoEntity.getCountryId().toString())
                .setDescription(photoEntity.getDescription() != null ? photoEntity.getDescription() : "")
                .setCreationDate(Timestamp.newBuilder()
                    .setSeconds(photoEntity.getCreatedDate().toInstant().getEpochSecond())
                    .setNanos(photoEntity.getCreatedDate().toInstant().getNano())
                )
                .setLikes(
                    Likes.newBuilder()
                        .setTotal(photoEntity.getLikes().size())
                        .addAllLikes(
                            photoEntity.getLikes().stream().map(likeEntity -> Like.newBuilder()
                                .setId(likeEntity.getId().toString())
                                .setUserId(likeEntity.getUserId().toString())
                                .setCreationDate(
                                    Timestamp.newBuilder()
                                        .setSeconds(likeEntity.getCreatedDate().toInstant().getEpochSecond())
                                        .setNanos(likeEntity.getCreatedDate().toInstant().getNano())
                                )
                                .build()
                            ).toList()
                        )
                )
                .build()
            ).toList()
        )
        .setHasNext(photos.hasNext())
        .build();
  }
}
