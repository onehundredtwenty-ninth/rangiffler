package com.onehundredtwentyninth.rangiffler.model;

import org.springframework.data.domain.Slice;

public record FeedJson(
    String username,
    Boolean withFriends,
    Slice<PhotoJson> photos,
    Object stat
) {

}
