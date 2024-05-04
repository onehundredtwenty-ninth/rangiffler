package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import javax.annotation.Nonnull;

public record GqlPhoto(
    UUID id,
    String src,
    GqlCountry country,
    String description,
    LocalDate creationDate,
    GqlLikes likes
) {

  public static @Nonnull GqlPhoto fromGrpcMessage(@Nonnull Photo photoMessage) {
    return new GqlPhoto(
        UUID.fromString(photoMessage.getId()),
        new String(photoMessage.getSrc().toByteArray(), StandardCharsets.UTF_8),
        new GqlCountry(UUID.fromString(photoMessage.getCountryId()), null, null, null),
        photoMessage.getDescription(),
        LocalDate.ofInstant(Instant.ofEpochSecond(photoMessage.getCreationDate().getSeconds()), ZoneId.of("UTC")),
        GqlLikes.fromGrpcMessage(photoMessage.getLikes())
    );
  }
}
