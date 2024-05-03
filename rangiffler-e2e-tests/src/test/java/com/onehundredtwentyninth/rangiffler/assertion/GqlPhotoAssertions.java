package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.gql.GqlLike;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlPhoto;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

public class GqlPhotoAssertions extends AbstractAssert<GqlPhotoAssertions, GqlPhoto> {

  public GqlPhotoAssertions(GqlPhoto gqlResponse) {
    super(gqlResponse, GqlPhotoAssertions.class);
  }

  public static GqlPhotoAssertions assertThat(GqlPhoto actual) {
    return new GqlPhotoAssertions(actual);
  }

  public GqlPhotoAssertions hasId(UUID id) {
    isNotNull();
    if (!id.equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public GqlPhotoAssertions idIsNotNull() {
    isNotNull();
    if (actual.getId() == null) {
      failWithActualExpectedAndMessage(actual, null, "Expected id to be not null, but ut was");
    }
    return this;
  }

  public GqlPhotoAssertions hasSrc(byte[] src) {
    isNotNull();
    var actualAvatar = actual.getSrc() == null ? new byte[]{} : actual.getSrc().getBytes(StandardCharsets.UTF_8);
    if (!Arrays.equals(src, actualAvatar)) {
      failWithActualExpectedAndMessage(actual, src, "Expected avatar to be <%s> but was <%s>", src,
          actual.getSrc());
    }
    return this;
  }

  public GqlPhotoAssertions hasCountryCode(String countryCode) {
    isNotNull();
    if (!countryCode.equals(actual.getCountry().getCode())) {
      failWithActualExpectedAndMessage(actual, countryCode, "Expected country code to be <%s> but was <%s>",
          countryCode,
          actual.getCountry().getCode());
    }
    return this;
  }

  public GqlPhotoAssertions hasDescription(String description) {
    isNotNull();
    if (!description.equals(actual.getDescription())) {
      failWithActualExpectedAndMessage(actual, description, "Expected description to be <%s> but was <%s>", description,
          actual.getDescription());
    }
    return this;
  }

  public GqlPhotoAssertions hasTotalLikes(Integer likesCount) {
    isNotNull();
    if (!Objects.equals(likesCount, actual.getLikes().getTotal())) {
      failWithActualExpectedAndMessage(actual, likesCount, "Expected likes count to be <%s> but was <%s>", likesCount,
          actual.getLikes().getTotal());
    }
    return this;
  }

  public GqlPhotoAssertions hasLikes(List<GqlLike> likes) {
    isNotNull();
    if (!actual.getLikes().getLikes().containsAll(likes)) {
      failWithActualExpectedAndMessage(actual.getLikes(), likes,
          "Expected likes contains all of <%s> but was <%s>", likes, actual.getLikes());
    }
    return this;
  }
}
