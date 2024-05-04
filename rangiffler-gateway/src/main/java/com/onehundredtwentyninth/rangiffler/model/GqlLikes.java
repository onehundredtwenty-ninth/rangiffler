package com.onehundredtwentyninth.rangiffler.model;

import com.onehundredtwentyninth.rangiffler.grpc.Likes;
import java.util.List;

public record GqlLikes(Integer total, List<GqlLike> likes) {

  public static GqlLikes fromGrpcMessage(Likes likesMessage) {
    return new GqlLikes(
        likesMessage.getTotal(),
        likesMessage.getLikesList().stream().map(GqlLike::fromGrpcMessage).toList()
    );
  }
}
