package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import org.springframework.data.domain.Slice;

public record GqlFeed(
    String username,
    Boolean withFriends,
    Slice<GqlPhoto> photos,
    List<GqlStat> stat
) {

}
