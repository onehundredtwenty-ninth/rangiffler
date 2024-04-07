package com.onehundredtwentyninth.rangiffler.exception;

import java.util.NoSuchElementException;

public class CountryNotFoundException extends NoSuchElementException {

  public CountryNotFoundException(String countryAttribute) {
    super("Country " + countryAttribute + " not found");
  }
}
