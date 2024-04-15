package com.onehundredtwentyninth.rangiffler.assertion;

import com.google.protobuf.Timestamp;
import com.onehundredtwentyninth.rangiffler.db.model.LikeEntity;
import com.onehundredtwentyninth.rangiffler.grpc.Like;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.AbstractAssert;

public class GrpcPhotoAssertions extends AbstractAssert<GrpcPhotoAssertions, Photo> {

  public GrpcPhotoAssertions(Photo photo) {
    super(photo, GrpcPhotoAssertions.class);
  }

  public static GrpcPhotoAssertions assertThat(Photo actual) {
    return new GrpcPhotoAssertions(actual);
  }

  public GrpcPhotoAssertions hasId(String id) {
    isNotNull();
    if (!id.equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public GrpcPhotoAssertions hasSrc(byte[] src) {
    isNotNull();
    if (!Arrays.equals(src, actual.getSrc().toByteArray())) {
      failWithActualExpectedAndMessage(actual, src, "Expected src to be <%s> but was <%s>", src, actual.getSrc());
    }
    return this;
  }

  public GrpcPhotoAssertions hasCountryId(String id) {
    isNotNull();
    if (!id.equals(actual.getCountryId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected countryId to be <%s> but was <%s>", id,
          actual.getCountryId());
    }
    return this;
  }

  public GrpcPhotoAssertions hasDescription(String description) {
    isNotNull();
    if (!description.equals(actual.getDescription())) {
      failWithActualExpectedAndMessage(actual, description, "Expected description to be <%s> but was <%s>", description,
          actual.getDescription());
    }
    return this;
  }

  public GrpcPhotoAssertions hasCreationDate(LocalDateTime creationDate) {
    isNotNull();
    var actualCreatedDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(actual.getCreationDate().getSeconds()),
        ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
    if (!creationDate.truncatedTo(ChronoUnit.SECONDS).equals(actualCreatedDate)) {
      failWithActualExpectedAndMessage(actual, creationDate, "Expected creationDate to be <%s> but was <%s>",
          creationDate,
          actualCreatedDate);
    }
    return this;
  }

  public GrpcPhotoAssertions hasTotalLikes(Integer likesCount) {
    isNotNull();
    if (likesCount != actual.getLikes().getLikesCount()) {
      failWithActualExpectedAndMessage(actual, likesCount, "Expected likes count to be <%s> but was <%s>", likesCount,
          actual.getLikes().getLikesCount());
    }
    return this;
  }

  public GrpcPhotoAssertions hasLikeFromUser(String userId) {
    isNotNull();
    boolean isLikeFromUserPresented = actual.getLikes().getLikesList().stream()
        .anyMatch(s -> userId.equals(s.getUserId()));
    if (!isLikeFromUserPresented) {
      failWithActualExpectedAndMessage(actual.getLikes(), userId,
          "Expected likes contains like from user <%s> but was <%s>", userId, actual.getLikes());
    }

    return this;
  }

  public GrpcPhotoAssertions hasLikes(List<LikeEntity> likes) {
    isNotNull();

    var expectedLikes = likes.stream().map(s ->
            Like.newBuilder()
                .setId(s.getId().toString())
                .setUserId(s.getUserId().toString())
                .setCreationDate(Timestamp.newBuilder()
                    .setSeconds(s.getCreatedDate().toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant().getEpochSecond())
                    .setNanos(s.getCreatedDate().toLocalDateTime().atZone(ZoneId.of("UTC")).toInstant().getNano())
                )
                .build()
        )
        .toList();

    if (!actual.getLikes().getLikesList().containsAll(expectedLikes)) {
      failWithActualExpectedAndMessage(actual.getLikes(), expectedLikes,
          "Expected likes contains all of <%s> but was <%s>", expectedLikes, actual.getLikes());
    }

    return this;
  }
}
