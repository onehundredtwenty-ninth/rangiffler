package com.onehundredtwentyninth.rangiffler.mapper;

import static com.onehundredtwentyninth.rangiffler.grpc.FriendStatus.FRIEND_STATUS_UNSPECIFIED;
import static com.onehundredtwentyninth.rangiffler.grpc.FriendStatus.NOT_FRIEND;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import com.onehundredtwentyninth.rangiffler.model.GqlFriendStatus;
import com.onehundredtwentyninth.rangiffler.model.GqlUser;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

  public static @Nonnull GqlUser fromGrpcMessage(@Nonnull User userMessage) {
    return new GqlUser(
        UUID.fromString(userMessage.getId()),
        userMessage.getUsername(),
        userMessage.getFirstname(),
        userMessage.getLastName(),
        new String(userMessage.getAvatar().toByteArray(), StandardCharsets.UTF_8),
        userMessage.getFriendStatus() == FRIEND_STATUS_UNSPECIFIED || userMessage.getFriendStatus() == NOT_FRIEND
            ? null
            : GqlFriendStatus.valueOf(userMessage.getFriendStatus().name()),
        null,
        null,
        null,
        new GqlCountry(UUID.fromString(userMessage.getCountryId()), null, null, null)
    );
  }
}
