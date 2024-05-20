package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.client.UsersClient;
import com.onehundredtwentyninth.rangiffler.model.FriendshipInput;
import com.onehundredtwentyninth.rangiffler.model.GqlLike;
import com.onehundredtwentyninth.rangiffler.model.GqlUser;
import com.onehundredtwentyninth.rangiffler.model.UserInput;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
public class UsersController {

  private final UsersClient usersClient;

  @Autowired
  public UsersController(UsersClient usersClient) {
    this.usersClient = usersClient;
  }

  @QueryMapping
  public Slice<GqlUser> users(@AuthenticationPrincipal Jwt principal,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getAllUsers(principal.getClaim("sub"), page, size, searchQuery);
  }

  @QueryMapping
  public GqlUser user(@AuthenticationPrincipal Jwt principal) {
    return usersClient.getUser(principal.getClaim("sub"));
  }

  @SchemaMapping(typeName = "User", field = "friends")
  public Slice<GqlUser> friends(GqlUser user,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getFriends(user.username(), page, size, searchQuery);
  }

  @SchemaMapping(typeName = "User", field = "incomeInvitations")
  public Slice<GqlUser> incomeInvitations(GqlUser user,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getFriendshipRequests(user.username(), page, size, searchQuery);
  }

  @SchemaMapping(typeName = "User", field = "outcomeInvitations")
  public Slice<GqlUser> outcomeInvitations(GqlUser user,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getFriendshipAddresses(user.username(), page, size, searchQuery);
  }

  @SchemaMapping(typeName = "Like", field = "username")
  public String likeUser(GqlLike like) {
    return usersClient.getUserById(like.user()).username();
  }

  @MutationMapping
  public GqlUser user(@AuthenticationPrincipal Jwt principal, @Argument UserInput input) {
    return usersClient.updateUser(principal.getClaim("sub"), input);
  }

  @MutationMapping
  public GqlUser friendship(@AuthenticationPrincipal Jwt principal, @Argument FriendshipInput input) {
    return usersClient.updateFriendshipStatus(principal.getClaim("sub"), input);
  }
}
