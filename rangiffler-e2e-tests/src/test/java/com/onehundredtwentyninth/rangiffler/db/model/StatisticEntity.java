package com.onehundredtwentyninth.rangiffler.db.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticEntity {

  private UUID id;
  private UUID userId;
  private UUID countryId;
  private Integer count;
}
