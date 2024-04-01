package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.UserJson;
import com.onehundredtwentyninth.rangiffler.service.UsersClient;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
}
