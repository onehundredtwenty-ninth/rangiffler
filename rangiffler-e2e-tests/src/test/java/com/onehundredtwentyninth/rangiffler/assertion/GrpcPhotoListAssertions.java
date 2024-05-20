package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.PhotoResponse;
import org.assertj.core.api.AbstractAssert;

public class GrpcPhotoListAssertions extends AbstractAssert<GrpcPhotoListAssertions, PhotoResponse> {

  public GrpcPhotoListAssertions(PhotoResponse photoResponse) {
    super(photoResponse, GrpcPhotoListAssertions.class);
  }

  public static GrpcPhotoListAssertions assertThat(PhotoResponse actual) {
    return new GrpcPhotoListAssertions(actual);
  }

  public GrpcPhotoListAssertions hasPageSize(Integer pageSize) {
    isNotNull();
    if (pageSize != actual.getPhotosCount()) {
      failWithActualExpectedAndMessage(actual, pageSize, "Expected page size to be <%s> but was <%s>", pageSize,
          actual.getPhotosCount());
    }
    return this;
  }

  public GrpcPhotoListAssertions hasNext(boolean hasNext) {
    isNotNull();
    if (hasNext != actual.getHasNext()) {
      failWithActualExpectedAndMessage(actual, hasNext, "Expected hasNext to be <%s> but was <%s>", hasNext,
          actual.getHasNext());
    }
    return this;
  }
}
