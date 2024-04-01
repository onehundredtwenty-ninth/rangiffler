package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import javax.annotation.Nonnull;

public record UserJson(
    UUID id,
    String username,
    @Size(max = 30, message = "First name can`t be longer than 30 characters")
    String firstname,
    @Size(max = 50, message = "Surname can`t be longer than 50 characters")
    String lastname,
    String avatar
) {

  public static @Nonnull UserJson fromGrpcMessage(@Nonnull User userMessage) {
    return new UserJson(
        UUID.fromString(userMessage.getId()),
        userMessage.getUsername(),
        userMessage.getFirstname(),
        userMessage.getLastName(),
        null
    );
  }
}
