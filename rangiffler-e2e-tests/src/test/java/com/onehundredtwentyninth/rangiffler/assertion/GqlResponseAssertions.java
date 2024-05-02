package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.GqlResponse;
import org.assertj.core.api.AbstractAssert;

public class GqlResponseAssertions extends AbstractAssert<GqlResponseAssertions, GqlResponse<?>> {

  public GqlResponseAssertions(GqlResponse gqlResponse) {
    super(gqlResponse, GqlResponseAssertions.class);
  }

  public static GqlResponseAssertions assertThat(GqlResponse<?> actual) {
    return new GqlResponseAssertions(actual);
  }

  public GqlResponseAssertions hasNotErrors() {
    isNotNull();
    if (actual.getErrors() != null) {
      failWithActualExpectedAndMessage(actual, null, "Expected errors to be <%s> but was <%s>", null,
          actual.getErrors());
    }
    return this;
  }

  public GqlResponseAssertions hasErrors() {
    isNotNull();
    if (actual.getErrors() == null) {
      failWithActualExpectedAndMessage(actual, null, "Expected errors to be not null but it was");
    }
    return this;
  }

  public GqlResponseAssertions hasErrorsCount(int expectedCount) {
    isNotNull();
    if (actual.getErrors() == null || actual.getErrors().size() != expectedCount) {
      failWithActualExpectedAndMessage(actual, null,
          "Expected errors to have size <%s> but was <%s>",
          expectedCount,
          actual.getErrors()
      );
    }
    return this;
  }

  public GqlResponseAssertions dataNotNull() {
    isNotNull();
    if (actual.getData() == null) {
      failWithActualExpectedAndMessage(actual, null, "Expected data to be not <%s> but was <%s>", null,
          null);
    }
    return this;
  }
}
