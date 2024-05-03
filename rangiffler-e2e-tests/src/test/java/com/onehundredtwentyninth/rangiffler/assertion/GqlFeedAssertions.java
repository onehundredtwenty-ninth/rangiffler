package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.gql.GqlFeed;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlStat;
import org.assertj.core.api.AbstractAssert;

public class GqlFeedAssertions extends AbstractAssert<GqlFeedAssertions, GqlFeed> {

  public GqlFeedAssertions(GqlFeed gqlResponse) {
    super(gqlResponse, GqlFeedAssertions.class);
  }

  public static GqlFeedAssertions assertThat(GqlFeed actual) {
    return new GqlFeedAssertions(actual);
  }

  public GqlFeedAssertions hasStatCount(String code, int count) {
    isNotNull();
    var actualStat = actual.getStat().stream().filter(s -> code.equals(s.getCountry().getCode())).findFirst();
    if (actualStat.isEmpty() || count != actualStat.get().getCount()) {
      failWithActualExpectedAndMessage(actual, count, "Expected stat count to be <%s> but was <%s>", count,
          actualStat.map(GqlStat::getCount).orElse(0));
    }
    return this;
  }
}
