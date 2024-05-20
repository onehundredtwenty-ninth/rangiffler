package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.Country;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

public class GrpcCountryAssertions extends AbstractAssert<GrpcCountryAssertions, Country> {

  protected GrpcCountryAssertions(Country country) {
    super(country, GrpcCountryAssertions.class);
  }

  public static GrpcCountryAssertions assertThat(Country actual) {
    return new GrpcCountryAssertions(actual);
  }

  public GrpcCountryAssertions hasId(UUID id) {
    isNotNull();
    if (!id.toString().equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public GrpcCountryAssertions hasCode(String code) {
    isNotNull();
    if (!code.equals(actual.getCode())) {
      failWithActualExpectedAndMessage(actual, code, "Expected code to be <%s> but was <%s>", code, actual.getCode());
    }
    return this;
  }

  public GrpcCountryAssertions hasName(String name) {
    isNotNull();
    if (!name.equals(actual.getName())) {
      failWithActualExpectedAndMessage(actual, name, "Expected name to be <%s> but was <%s>", name, actual.getName());
    }
    return this;
  }

  public GrpcCountryAssertions hasFlag(byte[] flag) {
    isNotNull();
    if (!Arrays.equals(flag, actual.getFlag().getBytes(StandardCharsets.UTF_8))) {
      failWithActualExpectedAndMessage(actual, flag, "Expected flag to be <%s> but was <%s>", flag, actual.getFlag());
    }
    return this;
  }
}
