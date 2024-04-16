package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.GqlCountryResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlResponse;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.assertj.core.api.StandardSoftAssertionsProvider;

public class GqlSoftAssertions extends AbstractSoftAssertions implements StandardSoftAssertionsProvider {

  public static void assertSoftly(Consumer<GqlSoftAssertions> consumer) {
    SoftAssertionsProvider.assertSoftly(GqlSoftAssertions.class, consumer);
  }

  public GqlResponseAssertions assertThat(GqlResponse<?> actual) {
    return proxy(GqlResponseAssertions.class, GqlResponse.class, actual);
  }

  public GqlCountryResponseAssertions assertThat(GqlCountryResponse actual) {
    return proxy(GqlCountryResponseAssertions.class, GqlCountryResponse.class, actual);
  }
}