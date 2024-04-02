package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.CountryJson;
import com.onehundredtwentyninth.rangiffler.model.FeedJson;
import com.onehundredtwentyninth.rangiffler.model.PhotoJson;
import com.onehundredtwentyninth.rangiffler.service.GeoClient;
import com.onehundredtwentyninth.rangiffler.service.PhotoClient;
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
  private final GeoClient geoClient;

  @Autowired
  public PhotoController(PhotoClient photoClient, GeoClient geoClient) {
    this.photoClient = photoClient;
    this.geoClient = geoClient;
  }

  @QueryMapping
  public FeedJson feed(@AuthenticationPrincipal Jwt principal,
      @Argument Boolean withFriends) {
    return new FeedJson(principal.getClaim("sub"), withFriends, null, null);
  }

  @SchemaMapping(typeName = "Feed", field = "photos")
  public Slice<PhotoJson> photos(FeedJson feed, @AuthenticationPrincipal Jwt principal,
      @Argument int page,
      @Argument int size) {
    return photoClient.getPhotos(principal.getClaim("sub"), page, size);
  }

  @SchemaMapping(typeName = "Photo", field = "country")
  public CountryJson country(PhotoJson photo) {
    return geoClient.getCountry(photo.country().id());
  }
}
