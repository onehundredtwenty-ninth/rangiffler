package com.onehundredtwentyninth.rangiffler.mapper;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoMapper {

  public static Photo toMessage(PhotoEntity photoEntity) {
    return Photo.newBuilder()
        .setId(photoEntity.getId().toString())
        .setSrc(ByteString.copyFrom(photoEntity.getPhoto() != null ? photoEntity.getPhoto() : new byte[]{}))
        .setCountryId(photoEntity.getCountryId().toString())
        .setDescription(photoEntity.getDescription() != null ? photoEntity.getDescription() : "")
        .setCreationDate(Timestamp.newBuilder()
            .setSeconds(photoEntity.getCreatedDate().toInstant().getEpochSecond())
            .setNanos(photoEntity.getCreatedDate().toInstant().getNano())
        )
        .setLikes(Likes.newBuilder().build())
        .build();
  }
}
