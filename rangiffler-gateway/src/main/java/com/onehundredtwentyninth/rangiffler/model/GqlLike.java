package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.Like;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public record GqlLike(
    UUID user,
    String userName,
    LocalDate creationDate
) {

  public static GqlLike fromGrpcMessage(Like likeMessage) {
    return new GqlLike(
        UUID.fromString(likeMessage.getUserId()),
        likeMessage.getUserId(),
        LocalDate.ofInstant(Instant.ofEpochSecond(likeMessage.getCreationDate().getSeconds()), ZoneId.of("UTC"))
    );
  }
}
