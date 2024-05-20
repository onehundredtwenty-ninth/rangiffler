package com.onehundredtwentyninth.rangiffler.exception;

import java.util.NoSuchElementException;
import java.util.UUID;

public class StatisticNotFoundException extends NoSuchElementException {

  public StatisticNotFoundException(UUID userId, UUID countryId) {
    super("Statistic for user " + userId + " and countryId " + countryId + " not found");
  }
}
