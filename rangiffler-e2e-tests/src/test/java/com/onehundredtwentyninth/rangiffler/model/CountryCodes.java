package com.onehundredtwentyninth.rangiffler.model;

import java.util.Locale;

public enum CountryCodes {
  US, CA, CN;

  public String getCode() {
    return name().toLowerCase(Locale.ROOT);
  }
}
