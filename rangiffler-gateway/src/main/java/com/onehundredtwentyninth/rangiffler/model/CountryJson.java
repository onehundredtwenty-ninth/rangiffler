package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.Country;
import javax.annotation.Nonnull;

public record CountryJson(
    String code,
    String name,
    String flag
) {

  public static @Nonnull CountryJson fromGrpcMessage(@Nonnull Country countryMessage) {
    return new CountryJson(
        countryMessage.getCode(),
        countryMessage.getName(),
        countryMessage.getFlag()
    );
  }
}
