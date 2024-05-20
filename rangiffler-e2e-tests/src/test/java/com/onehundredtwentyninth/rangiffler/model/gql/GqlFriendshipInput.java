package com.onehundredtwentyninth.rangiffler.model.gql;

import java.util.UUID;

public record GqlFriendshipInput(UUID user, GqlFriendshipAction action) {

}
