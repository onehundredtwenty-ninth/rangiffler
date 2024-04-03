package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.CountryJson;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import com.onehundredtwentyninth.rangiffler.service.GeoClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GeoController {

  private final GeoClient geoClient;

  @Autowired
  public GeoController(GeoClient geoClient) {
    this.geoClient = geoClient;
  }

  @QueryMapping
  public List<CountryJson> countries() {
    return geoClient.getAllCountries();
  }

  @SchemaMapping(typeName = "User", field = "location")
  public CountryJson userCountry(UserJson user) {
    return geoClient.getCountry(user.location().id());
  }
}
