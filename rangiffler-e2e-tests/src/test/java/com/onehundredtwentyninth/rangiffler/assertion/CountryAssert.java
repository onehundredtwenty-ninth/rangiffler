package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.Country;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

public class CountryAssert extends AbstractAssert<CountryAssert, Country> {

  protected CountryAssert(Country country) {
    super(country, CountryAssert.class);
  }

  public static CountryAssert assertThat(Country actual) {
    return new CountryAssert(actual);
  }

  public CountryAssert hasId(UUID id) {
    isNotNull();
    if (!id.toString().equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public CountryAssert hasCode(String code) {
    isNotNull();
    if (!code.equals(actual.getCode())) {
      failWithActualExpectedAndMessage(actual, code, "Expected code to be <%s> but was <%s>", code, actual.getCode());
    }
    return this;
  }

  public CountryAssert hasName(String name) {
    isNotNull();
    if (!name.equals(actual.getName())) {
      failWithActualExpectedAndMessage(actual, name, "Expected name to be <%s> but was <%s>", name, actual.getName());
    }
    return this;
  }

  public CountryAssert hasFlag(byte[] flag) {
    isNotNull();
    if (!Arrays.equals(flag, actual.getFlag().getBytes(StandardCharsets.UTF_8))) {
      failWithActualExpectedAndMessage(actual, flag, "Expected flag to be <%s> but was <%s>", flag, actual.getFlag());
    }
    return this;
  }
}
