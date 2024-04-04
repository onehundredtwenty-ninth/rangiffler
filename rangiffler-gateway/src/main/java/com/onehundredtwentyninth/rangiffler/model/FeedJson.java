package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import org.springframework.data.domain.Slice;

public record FeedJson(
    String username,
    Boolean withFriends,
    Slice<PhotoJson> photos,
    List<StatJson> stat
) {

}
