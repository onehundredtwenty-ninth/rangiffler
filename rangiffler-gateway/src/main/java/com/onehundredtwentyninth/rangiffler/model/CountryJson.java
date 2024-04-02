package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onehundredtwentyninth.rangiffler.grpc.Country;
import java.util.UUID;
import javax.annotation.Nonnull;

public record CountryJson(
    @JsonIgnore
    UUID id,
    String code,
    String name,
    String flag
) {

  public static @Nonnull CountryJson fromGrpcMessage(@Nonnull Country countryMessage) {
    return new CountryJson(
        UUID.fromString(countryMessage.getId()),
        countryMessage.getCode(),
        countryMessage.getName(),
        countryMessage.getFlag()
    );
  }
}
