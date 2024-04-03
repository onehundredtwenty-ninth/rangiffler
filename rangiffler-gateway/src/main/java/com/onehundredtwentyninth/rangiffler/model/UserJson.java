package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.springframework.data.domain.Slice;

public record UserJson(
    UUID id,
    String username,
    String firstname,
    String surname,
    String avatar,
    FriendStatus friendStatus,
    Slice<UserJson> friends,
    Slice<UserJson> incomeInvitations,
    Slice<UserJson> outcomeInvitations,
    CountryJson location
) {

  public static @Nonnull UserJson fromGrpcMessage(@Nonnull User userMessage) {
    return new UserJson(
        UUID.fromString(userMessage.getId()),
        userMessage.getUsername(),
        userMessage.getFirstname(),
        userMessage.getLastName(),
        new String(userMessage.getAvatar().toByteArray(), StandardCharsets.UTF_8),
        null,
        null,
        null,
        null,
        new CountryJson(UUID.fromString(userMessage.getCountryId()), null, null, null)
    );
  }

  public static @Nonnull UserJson friendFromGrpcMessage(@Nonnull User userMessage, FriendStatus friendStatus) {
    return new UserJson(
        UUID.fromString(userMessage.getId()),
        userMessage.getUsername(),
        userMessage.getFirstname(),
        userMessage.getLastName(),
        new String(userMessage.getAvatar().toByteArray(), StandardCharsets.UTF_8),
        friendStatus,
        null,
        null,
        null,
        new CountryJson(UUID.fromString(userMessage.getCountryId()), null, null, null)
    );
  }
}
