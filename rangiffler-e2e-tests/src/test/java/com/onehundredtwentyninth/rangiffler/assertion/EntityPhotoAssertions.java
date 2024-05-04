package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import java.util.Arrays;
import org.assertj.core.api.AbstractAssert;

public class EntityPhotoAssertions extends AbstractAssert<EntityPhotoAssertions, PhotoEntity> {

  public EntityPhotoAssertions(PhotoEntity photo) {
    super(photo, EntityPhotoAssertions.class);
  }

  public static EntityPhotoAssertions assertThat(PhotoEntity actual) {
    return new EntityPhotoAssertions(actual);
  }

  public EntityPhotoAssertions hasId(String id) {
    isNotNull();
    if (!id.equals(actual.getId().toString())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public EntityPhotoAssertions hasUserId(String id) {
    isNotNull();
    if (!id.equals(actual.getUserId().toString())) {
      failWithActualExpectedAndMessage(actual, id, "Expected userId to be <%s> but was <%s>", id, actual.getUserId());
    }
    return this;
  }

  public EntityPhotoAssertions hasSrc(byte[] src) {
    isNotNull();
    if (!Arrays.equals(src, actual.getPhoto())) {
      failWithActualExpectedAndMessage(actual, src, "Expected src to be <%s> but was <%s>", src, actual.getPhoto());
    }
    return this;
  }

  public EntityPhotoAssertions hasCountryId(String id) {
    isNotNull();
    if (!id.equals(actual.getCountryId().toString())) {
      failWithActualExpectedAndMessage(actual, id, "Expected countryId to be <%s> but was <%s>", id,
          actual.getCountryId());
    }
    return this;
  }

  public EntityPhotoAssertions hasDescription(String description) {
    isNotNull();
    if (!description.equals(actual.getDescription())) {
      failWithActualExpectedAndMessage(actual, description, "Expected description to be <%s> but was <%s>", description,
          actual.getDescription());
    }
    return this;
  }
}
