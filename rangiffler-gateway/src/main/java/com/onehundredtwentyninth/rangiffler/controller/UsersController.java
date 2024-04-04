package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.LikeJson;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import com.onehundredtwentyninth.rangiffler.service.UsersClient;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
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
  public Slice<UserJson> users(@AuthenticationPrincipal Jwt principal,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getAllUsers(principal.getClaim("sub"), page, size, searchQuery);
  }

  @QueryMapping
  public UserJson user(@AuthenticationPrincipal Jwt principal) {
    return usersClient.getUser(principal.getClaim("sub"));
  }

  @SchemaMapping(typeName = "User", field = "friends")
  public Slice<UserJson> friends(UserJson user,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getFriends(user.username(), page, size, searchQuery);
  }

  @SchemaMapping(typeName = "User", field = "incomeInvitations")
  public Slice<UserJson> incomeInvitations(UserJson user,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getFriendshipRequests(user.username(), page, size, searchQuery);
  }

  @SchemaMapping(typeName = "User", field = "outcomeInvitations")
  public Slice<UserJson> outcomeInvitations(UserJson user,
      @Argument int page,
      @Argument int size,
      @Argument @Nullable String searchQuery) {
    return usersClient.getFriendshipAddresses(user.username(), page, size, searchQuery);
  }

  @SchemaMapping(typeName = "Like", field = "username")
  public String likeUser(LikeJson like) {
    return usersClient.getUserById(like.user()).username();
  }
}
