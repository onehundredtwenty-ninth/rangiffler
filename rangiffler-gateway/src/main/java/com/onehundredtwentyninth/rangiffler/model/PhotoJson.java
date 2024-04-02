package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import javax.annotation.Nonnull;

public record PhotoJson(
    UUID id,
    String src,
    CountryJson country,
    String description,
    LocalDate creationDate,
    LikesJson likes
) {

  public static @Nonnull PhotoJson fromGrpcMessage(@Nonnull Photo photoMessage) {
    return new PhotoJson(
        UUID.fromString(photoMessage.getId()),
        new String(photoMessage.getSrc().toByteArray(), StandardCharsets.UTF_8),
        new CountryJson(UUID.fromString(photoMessage.getCountryId()), null, null, null),
        photoMessage.getDescription(),
        LocalDate.ofInstant(Instant.ofEpochSecond(photoMessage.getCreationDate().getSeconds()), ZoneId.of("UTC")),
        LikesJson.fromGrpcMessage(photoMessage.getLikes())
    );
  }
}
