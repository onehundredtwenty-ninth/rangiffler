package com.onehundredtwentyninth.rangiffler.service;

import com.google.protobuf.BoolValue;
import com.onehundredtwentyninth.rangiffler.data.LikeEntity;
import com.onehundredtwentyninth.rangiffler.data.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.data.StatisticEntity;
import com.onehundredtwentyninth.rangiffler.data.repository.LikeRepository;
import com.onehundredtwentyninth.rangiffler.data.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.data.repository.StatisticRepository;
import com.onehundredtwentyninth.rangiffler.exception.IllegalPhotoAccessException;
import com.onehundredtwentyninth.rangiffler.exception.PhotoNotFoundException;
import com.onehundredtwentyninth.rangiffler.exception.StatisticNotFoundException;
import com.onehundredtwentyninth.rangiffler.grpc.CreatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.LikePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.mapper.PhotoMapper;
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
  private final LikeRepository likeRepository;
  private final StatisticRepository statisticRepository;

  @Autowired
  public PhotoService(PhotoRepository photoRepository, LikeRepository likeRepository,
      StatisticRepository statisticRepository) {
    this.photoRepository = photoRepository;
    this.statisticRepository = statisticRepository;
    this.likeRepository = likeRepository;
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
    increaseStatistic(photoEntity.getUserId(), photoEntity.getCountryId());

    var photoResponse = PhotoMapper.toMessage(photoEntity);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void updatePhoto(UpdatePhotoRequest request, StreamObserver<Photo> responseObserver) {
    var photoEntity = photoRepository.findById(UUID.fromString(request.getId()))
        .orElseThrow(() -> new PhotoNotFoundException(request.getId()));
    var originalCountryId = photoEntity.getCountryId();

    if (!photoEntity.getUserId().equals(UUID.fromString(request.getUserId()))) {
      throw new IllegalPhotoAccessException(photoEntity.getId().toString(), request.getUserId());
    }

    photoEntity.setCountryId(UUID.fromString(request.getCountryId()));
    photoEntity.setDescription(request.getDescription());

    photoRepository.save(photoEntity);
    if (!originalCountryId.equals(photoEntity.getCountryId())) {
      increaseStatistic(photoEntity.getUserId(), photoEntity.getCountryId());
      decreaseStatistic(photoEntity.getUserId(), originalCountryId);
    }

    var photoResponse = PhotoMapper.toMessage(photoEntity);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void likePhoto(LikePhotoRequest request, StreamObserver<Photo> responseObserver) {
    var photoEntity = photoRepository.findById(UUID.fromString(request.getPhotoId()))
        .orElseThrow(() -> new PhotoNotFoundException(request.getPhotoId()));

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
      likeRepository.delete(existedLikeEntity.get());
      photoRepository.save(photoEntity);
    }

    var photoResponse = PhotoMapper.toMessage(photoEntity);
    responseObserver.onNext(photoResponse);
    responseObserver.onCompleted();
  }

  @Transactional
  @Override
  public void deletePhoto(DeletePhotoRequest request, StreamObserver<BoolValue> responseObserver) {
    var photoEntity = photoRepository.findById(UUID.fromString(request.getPhotoId()))
        .orElseThrow(() -> new PhotoNotFoundException(request.getPhotoId()));

    if (!photoEntity.getUserId().equals(UUID.fromString(request.getUserId()))) {
      throw new IllegalPhotoAccessException(photoEntity.getId().toString(), request.getUserId());
    }

    photoRepository.delete(photoEntity);
    decreaseStatistic(photoEntity.getUserId(), photoEntity.getCountryId());

    responseObserver.onNext(BoolValue.of(true));
    responseObserver.onCompleted();
  }

  private void increaseStatistic(UUID userId, UUID countryId) {
    var statisticEntity = statisticRepository.findByUserIdAndCountryId(userId,
        countryId);
    if (statisticEntity.isPresent()) {
      statisticEntity.get().setCount(statisticEntity.get().getCount() + 1);
      statisticRepository.save(statisticEntity.get());
    } else {
      var newStatisticEntity = new StatisticEntity();
      newStatisticEntity.setUserId(userId);
      newStatisticEntity.setCountryId(countryId);
      newStatisticEntity.setCount(1);
      statisticRepository.save(newStatisticEntity);
    }
  }

  private void decreaseStatistic(UUID userId, UUID countryId) {
    var statisticEntity = statisticRepository.findByUserIdAndCountryId(userId,
        countryId).orElseThrow(() -> new StatisticNotFoundException(userId, countryId));

    if (statisticEntity.getCount() - 1 == 0) {
      statisticRepository.delete(statisticEntity);
    } else {
      statisticEntity.setCount(statisticEntity.getCount() - 1);
      statisticRepository.save(statisticEntity);
    }
  }
}
