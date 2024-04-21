package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.GqlConnection;
import org.assertj.core.api.AbstractAssert;

public class GqlConnectionAssertions extends AbstractAssert<GqlConnectionAssertions, GqlConnection<?>> {

  public GqlConnectionAssertions(GqlConnection<?> gqlResponse) {
    super(gqlResponse, GqlConnectionAssertions.class);
  }

  public static GqlConnectionAssertions assertThat(GqlConnection<?> actual) {
    return new GqlConnectionAssertions(actual);
  }

  public GqlConnectionAssertions hasEdgesCount(int count) {
    isNotNull();
    if (count != actual.getEdges().size()) {
      failWithActualExpectedAndMessage(actual, count, "Expected edges count to be <%s> but was <%s>", count,
          actual.getEdges().size());
    }
    return this;
  }

  public GqlConnectionAssertions hasPrevious(boolean previous) {
    isNotNull();
    if (previous != actual.getPageInfo().hasPreviousPage()) {
      failWithActualExpectedAndMessage(actual, previous, "Expected page info previous to be <%s> but was <%s>",
          previous,
          actual.getPageInfo().hasPreviousPage()
      );
    }
    return this;
  }

  public GqlConnectionAssertions hasNext(boolean next) {
    isNotNull();
    if (next != actual.getPageInfo().hasNextPage()) {
      failWithActualExpectedAndMessage(actual, next, "Expected page info next to be <%s> but was <%s>",
          next,
          actual.getPageInfo().hasNextPage()
      );
    }
    return this;
  }
}
