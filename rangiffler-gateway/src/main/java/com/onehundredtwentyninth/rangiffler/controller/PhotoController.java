package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.client.PhotoClient;
import com.onehundredtwentyninth.rangiffler.model.GqlFeed;
import com.onehundredtwentyninth.rangiffler.model.GqlPhoto;
import com.onehundredtwentyninth.rangiffler.model.PhotoInput;
import java.util.UUID;
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
public class PhotoController {

  private final PhotoClient photoClient;

  @Autowired
  public PhotoController(PhotoClient photoClient) {
    this.photoClient = photoClient;
  }

  @QueryMapping
  public GqlFeed feed(@AuthenticationPrincipal Jwt principal,
      @Argument Boolean withFriends) {
    return new GqlFeed(principal.getClaim("sub"), withFriends, null, null);
  }

  @SchemaMapping(typeName = "Feed", field = "photos")
  public Slice<GqlPhoto> photos(GqlFeed feed, @AuthenticationPrincipal Jwt principal,
      @Argument int page,
      @Argument int size) {
    return photoClient.getPhotos(principal.getClaim("sub"), page, size, feed.withFriends());
  }

  @MutationMapping
  public GqlPhoto photo(@AuthenticationPrincipal Jwt principal, @Argument PhotoInput input) {
    return photoClient.photo(principal.getClaim("sub"), input);
  }

  @MutationMapping
  public boolean deletePhoto(@AuthenticationPrincipal Jwt principal, @Argument UUID id) {
    return photoClient.deletePhoto(principal.getClaim("sub"), id);
  }
}
