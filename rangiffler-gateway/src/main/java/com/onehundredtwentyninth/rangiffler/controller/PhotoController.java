package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.FeedJson;
import com.onehundredtwentyninth.rangiffler.model.PhotoJson;
import com.onehundredtwentyninth.rangiffler.service.PhotoClient;
import org.apache.hc.core5.http.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
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
  public FeedJson feed(@AuthenticationPrincipal Jwt principal,
      @Argument Boolean withFriends) throws NotImplementedException {
    throw new NotImplementedException();
  }

  @SchemaMapping(typeName = "Photo", field = "photos")
  public Slice<PhotoJson> photos(FeedJson feed, @AuthenticationPrincipal Jwt principal,
      @Argument int page,
      @Argument int size) {
    return photoClient.getPhotos(principal.getClaim("sub"), page, size);
  }
}
