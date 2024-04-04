package com.onehundredtwentyninth.rangiffler.controller;

import com.onehundredtwentyninth.rangiffler.model.FeedJson;
import com.onehundredtwentyninth.rangiffler.model.StatJson;
import com.onehundredtwentyninth.rangiffler.service.StatisticClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
public class StatisticController {

  private final StatisticClient statisticClient;

  @Autowired
  public StatisticController(StatisticClient statisticClient) {
    this.statisticClient = statisticClient;
  }

  @SchemaMapping(typeName = "Feed", field = "stat")
  public List<StatJson> stat(FeedJson feed, @AuthenticationPrincipal Jwt principal) {
    return statisticClient.getStatistic(principal.getClaim("sub"), feed.withFriends());
  }
}
