package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.Country;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.assertj.core.api.StandardSoftAssertionsProvider;

public class GrpcResponseSoftAssertions extends AbstractSoftAssertions implements StandardSoftAssertionsProvider {

  public static void assertSoftly(Consumer<GrpcResponseSoftAssertions> consumer) {
    SoftAssertionsProvider.assertSoftly(GrpcResponseSoftAssertions.class, consumer);
  }

  public CountryAssert assertThat(Country actual) {
    return proxy(CountryAssert.class, Country.class, actual);
  }
}
