package com.onehundredtwentyninth.rangiffler.mapper;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestCountry;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestPhoto;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoMapper {

  public static TestPhoto toTestPhoto(PhotoEntity photoEntity) {
    return TestPhoto.builder()
        .id(photoEntity.getId())
        .photo(photoEntity.getPhoto())
        .country(new TestCountry(photoEntity.getCountryId(), null, null, null))
        .description(photoEntity.getDescription() != null ? photoEntity.getDescription() : "")
        .createdDate(photoEntity.getCreatedDate().toLocalDateTime())
        .likes(Collections.emptyList())
        .build();
  }

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
