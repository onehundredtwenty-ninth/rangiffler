package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.Country;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.PhotoResponse;
import com.onehundredtwentyninth.rangiffler.grpc.User;
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

  public UserAssert assertThat(User actual) {
    return proxy(UserAssert.class, User.class, actual);
  }

  public AllUsersAssert assertThat(AllUsersResponse actual) {
    return proxy(AllUsersAssert.class, AllUsersResponse.class, actual);
  }

  public GrpcPhotoAssertions assertThat(Photo actual) {
    return proxy(GrpcPhotoAssertions.class, Photo.class, actual);
  }

  public GrpcPhotoListAssertions assertThat(PhotoResponse actual) {
    return proxy(GrpcPhotoListAssertions.class, PhotoResponse.class, actual);
  }
}
