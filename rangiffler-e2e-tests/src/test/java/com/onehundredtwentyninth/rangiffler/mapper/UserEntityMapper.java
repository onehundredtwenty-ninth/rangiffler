package com.onehundredtwentyninth.rangiffler.mapper;

import com.google.protobuf.ByteString;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntityMapper {

  public static User toMessage(UserEntity entity) {
    return User.newBuilder()
        .setId(entity.getId().toString())
        .setUsername(entity.getUsername())
        .setFirstname(entity.getFirstname())
        .setLastName(entity.getLastName())
        .setAvatar(ByteString.copyFrom(entity.getAvatar() != null ? entity.getAvatar() : new byte[]{}))
        .setCountryId(entity.getCountryId().toString())
        .build();
  }

  public static UserEntity toEntity(User message) {
    var userEntity = new UserEntity();
    userEntity.setId(UUID.fromString(message.getId()));
    userEntity.setUsername(message.getUsername());
    userEntity.setLastName(message.getLastName());
    userEntity.setAvatar(message.getAvatar().toByteArray());
    userEntity.setCountryId(UUID.fromString(message.getCountryId()));
    return userEntity;
  }
}
