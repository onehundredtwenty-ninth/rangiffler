package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import com.onehundredtwentyninth.rangiffler.model.GqlCountryResponse;
import org.assertj.core.api.AbstractAssert;

public class GqlCountryResponseAssertions extends AbstractAssert<GqlCountryResponseAssertions, GqlCountryResponse> {

  public GqlCountryResponseAssertions(GqlCountryResponse gqlResponse) {
    super(gqlResponse, GqlCountryResponseAssertions.class);
  }

  public static GqlCountryResponseAssertions assertThat(GqlCountryResponse actual) {
    return new GqlCountryResponseAssertions(actual);
  }

  public GqlCountryResponseAssertions countriesNotNull() {
    isNotNull();
    if (actual.getCountries() == null) {
      failWithActualExpectedAndMessage(actual, null, "Expected countries not to be <%s> but was <%s>",
          null,
          null);
    }
    return this;
  }

  public GqlCountryResponseAssertions hasCountriesCount(int expectedCount) {
    isNotNull();
    if (actual.getCountries().size() != expectedCount) {
      failWithActualExpectedAndMessage(actual, expectedCount, "Expected countries count to be <%s> but was <%s>",
          expectedCount,
          actual.getCountries().size());
    }
    return this;
  }

  public GqlCountryResponseAssertions containsCountry(GqlCountry expectedCountry) {
    isNotNull();
    if (!actual.getCountries().contains(expectedCountry)) {
      failWithActualExpectedAndMessage(actual, expectedCountry, "Expected countries contain <%s> but it's not",
          expectedCountry);
    }
    return this;
  }
}
