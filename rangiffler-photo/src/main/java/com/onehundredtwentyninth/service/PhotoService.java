package com.onehundredtwentyninth.service;

import com.google.protobuf.BoolValue;
import com.onehundredtwentyninth.data.LikeEntity;
import com.onehundredtwentyninth.data.PhotoEntity;
import com.onehundredtwentyninth.data.repository.PhotoRepository;
import com.onehundredtwentyninth.mapper.PhotoMapper;
import com.onehundredtwentyninth.rangiffler.grpc.CreatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.LikePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
import io.grpc.stub.StreamObserver;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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

    var photoResponse = PhotoResponse.newBuilder()
        .addAllPhotos(photos.map(PhotoMapper::toMessage).toList())
        .setHasNext(photos.hasNext())
        .build();
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void createPhoto(CreatePhotoRequest request, StreamObserver<Photo> responseObserver) {
    var photoEntity = new PhotoEntity();

    photoEntity.setUserId(UUID.fromString(request.getUserId()));
    photoEntity.setPhoto(request.getSrc().toByteArray());
    photoEntity.setCountryId(UUID.fromString(request.getCountryId()));
    photoEntity.setDescription(request.getDescription());
    photoEntity.setCreatedDate(Timestamp.from(Instant.now()));

    photoRepository.save(photoEntity);

    var photoResponse = PhotoMapper.toMessage(photoEntity);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void updatePhoto(UpdatePhotoRequest request, StreamObserver<Photo> responseObserver) {
    var photoEntity = photoRepository.findById(UUID.fromString(request.getId())).orElseThrow();

    if (!photoEntity.getUserId().equals(UUID.fromString(request.getUserId()))) {
      throw new IllegalStateException();
    }

    photoEntity.setPhoto(request.getSrc().toByteArray());
    photoEntity.setCountryId(UUID.fromString(request.getCountryId()));
    photoEntity.setDescription(request.getDescription());

    photoRepository.save(photoEntity);

    var photoResponse = PhotoMapper.toMessage(photoEntity);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void likePhoto(LikePhotoRequest request, StreamObserver<Photo> responseObserver) {
    var photoEntity = photoRepository.findById(UUID.fromString(request.getPhotoId())).orElseThrow();

    var existedLikeEntity = photoEntity.getLikes().stream()
        .filter(s -> UUID.fromString(request.getUserId()).equals(s.getUserId()))
        .findFirst();
    if (existedLikeEntity.isEmpty()) {
      var likeEntity = new LikeEntity();
      likeEntity.setUserId(UUID.fromString(request.getUserId()));
      likeEntity.setCreatedDate(Timestamp.from(Instant.now()));
      photoEntity.getLikes().add(likeEntity);
      photoRepository.save(photoEntity);
    } else {
      photoEntity.getLikes().remove(existedLikeEntity.get());
      photoRepository.save(photoEntity);
    }

    var photoResponse = PhotoMapper.toMessage(photoEntity);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void deletePhoto(DeletePhotoRequest request, StreamObserver<BoolValue> responseObserver) {
    var photoEntity = photoRepository.findById(UUID.fromString(request.getPhotoId())).orElseThrow();

    if (!photoEntity.getUserId().equals(UUID.fromString(request.getUserId()))) {
      throw new IllegalStateException();
    }

    photoRepository.delete(photoEntity);
    responseObserver.onNext(BoolValue.of(true));
    responseObserver.onCompleted();
  }
}
