package com.onehundredtwentyninth.rangiffler.mapper;

import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import com.onehundredtwentyninth.rangiffler.model.GqlPhoto;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoMapper {

  public static @Nonnull GqlPhoto fromGrpcMessage(@Nonnull Photo photoMessage) {
    return new GqlPhoto(
        UUID.fromString(photoMessage.getId()),
        new String(photoMessage.getSrc().toByteArray(), StandardCharsets.UTF_8),
        new GqlCountry(UUID.fromString(photoMessage.getCountryId()), null, null, null),
        photoMessage.getDescription(),
        LocalDate.ofInstant(Instant.ofEpochSecond(photoMessage.getCreationDate().getSeconds()), ZoneId.of("UTC")),
        LikeMapper.fromGrpcMessage(photoMessage.getLikes())
    );
  }
}
