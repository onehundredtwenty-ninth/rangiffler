package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import java.util.List;

public record LikesJson(Integer total, List<LikeJson> likes) {

  public static LikesJson fromGrpcMessage(Likes likesMessage) {
    return new LikesJson(
        likesMessage.getTotal(),
        likesMessage.getLikesList().stream().map(LikeJson::fromGrpcMessage).toList()
    );
  }
}
