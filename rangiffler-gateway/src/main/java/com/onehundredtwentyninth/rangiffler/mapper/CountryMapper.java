package com.onehundredtwentyninth.rangiffler.mapper;

import com.onehundredtwentyninth.rangiffler.grpc.Country;
import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import java.util.UUID;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryMapper {

  public static @Nonnull GqlCountry fromGrpcMessage(@Nonnull Country countryMessage) {
    return new GqlCountry(
        UUID.fromString(countryMessage.getId()),
        countryMessage.getCode(),
        countryMessage.getName(),
        countryMessage.getFlag()
    );
  }
}
