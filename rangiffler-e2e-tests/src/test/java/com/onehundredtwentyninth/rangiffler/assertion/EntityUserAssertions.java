package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import java.util.Arrays;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

public class EntityUserAssertions extends AbstractAssert<EntityUserAssertions, UserEntity> {

  public EntityUserAssertions(UserEntity entity) {
    super(entity, EntityUserAssertions.class);
  }

  public static EntityUserAssertions assertThat(UserEntity actual) {
    return new EntityUserAssertions(actual);
  }

  public EntityUserAssertions hasId(UUID id) {
    isNotNull();
    if (!id.equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public EntityUserAssertions hasUsername(String username) {
    isNotNull();
    if (!username.equals(actual.getUsername())) {
      failWithActualExpectedAndMessage(actual, username, "Expected username to be <%s> but was <%s>", username,
          actual.getUsername());
    }
    return this;
  }

  public EntityUserAssertions hasFirstName(String firstName) {
    isNotNull();
    if (!firstName.equals(actual.getFirstname())) {
      failWithActualExpectedAndMessage(actual, firstName, "Expected firstName to be <%s> but was <%s>", firstName,
          actual.getFirstname());
    }
    return this;
  }

  public EntityUserAssertions hasLastName(String lastName) {
    isNotNull();
    if (!lastName.equals(actual.getLastName())) {
      failWithActualExpectedAndMessage(actual, lastName, "Expected surname to be <%s> but was <%s>", lastName,
          actual.getLastName());
    }
    return this;
  }

  public EntityUserAssertions firstNameIsNull() {
    isNotNull();
    if (actual.getFirstname() != null) {
      failWithActualExpectedAndMessage(actual, null, "Expected firstName to be null but was <%s>",
          actual.getFirstname());
    }
    return this;
  }

  public EntityUserAssertions lastNameIsNull() {
    isNotNull();
    if (actual.getLastName() != null) {
      failWithActualExpectedAndMessage(actual, null, "Expected surname to be null but was <%s>",
          actual.getLastName());
    }
    return this;
  }

  public EntityUserAssertions hasAvatar(byte[] avatar) {
    isNotNull();
    if (!Arrays.equals(avatar, actual.getAvatar())) {
      failWithActualExpectedAndMessage(actual, avatar, "Expected avatar to be <%s> but was <%s>", avatar,
          actual.getAvatar());
    }
    return this;
  }

  public EntityUserAssertions hasCountryId(UUID countryId) {
    isNotNull();
    if (!countryId.equals(actual.getCountryId())) {
      failWithActualExpectedAndMessage(actual, countryId, "Expected countryId to be <%s> but was <%s>", countryId,
          actual.getCountryId());
    }
    return this;
  }
}
