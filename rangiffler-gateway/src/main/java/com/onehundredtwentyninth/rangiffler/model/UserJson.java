package com.onehundredtwentyninth.rangiffler.model;

import static com.onehundredtwentyninth.rangiffler.grpc.FriendStatus.FRIEND_STATUS_UNSPECIFIED;
import static com.onehundredtwentyninth.rangiffler.grpc.FriendStatus.NOT_FRIEND;

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
        userMessage.getFriendStatus() == FRIEND_STATUS_UNSPECIFIED || userMessage.getFriendStatus() == NOT_FRIEND
            ? null
            : FriendStatus.valueOf(userMessage.getFriendStatus().name()),
        null,
        null,
        null,
        new CountryJson(UUID.fromString(userMessage.getCountryId()), null, null, null)
    );
  }
}
