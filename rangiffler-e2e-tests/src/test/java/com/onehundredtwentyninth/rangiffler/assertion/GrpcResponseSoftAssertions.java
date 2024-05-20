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

  public GrpcCountryAssertions assertThat(Country actual) {
    return proxy(GrpcCountryAssertions.class, Country.class, actual);
  }

  public GrpcUserAssertions assertThat(User actual) {
    return proxy(GrpcUserAssertions.class, User.class, actual);
  }

  public GrpcUsersListAssertions assertThat(AllUsersResponse actual) {
    return proxy(GrpcUsersListAssertions.class, AllUsersResponse.class, actual);
  }

  public GrpcPhotoAssertions assertThat(Photo actual) {
    return proxy(GrpcPhotoAssertions.class, Photo.class, actual);
  }

  public GrpcPhotoListAssertions assertThat(PhotoResponse actual) {
    return proxy(GrpcPhotoListAssertions.class, PhotoResponse.class, actual);
  }
}
