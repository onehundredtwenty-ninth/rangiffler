package com.onehundredtwentyninth.rangiffler.model;

import java.util.Locale;

public enum CountryCodes {
  US, CA, CN, MX, DE;

  public String getCode() {
    return name().toLowerCase(Locale.ROOT);
  }
}
