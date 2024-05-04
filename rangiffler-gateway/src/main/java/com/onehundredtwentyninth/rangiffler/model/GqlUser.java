package com.onehundredtwentyninth.rangiffler.model;

import java.util.UUID;
import org.springframework.data.domain.Slice;

public record GqlUser(
    UUID id,
    String username,
    String firstname,
    String surname,
    String avatar,
    GqlFriendStatus friendStatus,
    Slice<GqlUser> friends,
    Slice<GqlUser> incomeInvitations,
    Slice<GqlUser> outcomeInvitations,
    GqlCountry location
) {

}
