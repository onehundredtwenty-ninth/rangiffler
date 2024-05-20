package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.gql.GqlConnection;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlCountryResponse;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlError;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlFeed;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlPhoto;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlResponse;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlUser;
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

  public GqlUserAssertions assertThat(GqlUser actual) {
    return proxy(GqlUserAssertions.class, GqlUser.class, actual);
  }

  public GqlConnectionAssertions assertThat(GqlConnection<?> actual) {
    return proxy(GqlConnectionAssertions.class, GqlConnection.class, actual);
  }

  public GqlPhotoAssertions assertThat(GqlPhoto actual) {
    return proxy(GqlPhotoAssertions.class, GqlPhoto.class, actual);
  }

  public GqlFeedAssertions assertThat(GqlFeed actual) {
    return proxy(GqlFeedAssertions.class, GqlFeed.class, actual);
  }

  public GqlResponseErrorsAssertions assertThat(GqlError actual) {
    return proxy(GqlResponseErrorsAssertions.class, GqlError.class, actual);
  }
}
