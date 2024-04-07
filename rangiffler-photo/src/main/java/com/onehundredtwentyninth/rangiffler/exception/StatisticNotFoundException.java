package com.onehundredtwentyninth.rangiffler.exception;

import java.util.UUID;

public class StatisticNotFoundException extends IllegalArgumentException {

  public StatisticNotFoundException(UUID userId, UUID countryId) {
    super("Statistic for user " + userId + " and countryId " + countryId + " not found");
  }
}
