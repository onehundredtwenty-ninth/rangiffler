package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import java.util.Arrays;
import org.assertj.core.api.AbstractAssert;

public class UserAssert extends AbstractAssert<UserAssert, User> {

  protected UserAssert(User user) {
    super(user, UserAssert.class);
  }

  public static UserAssert assertThat(User actual) {
    return new UserAssert(actual);
  }

  public UserAssert hasId(String id) {
    isNotNull();
    if (!id.equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public UserAssert hasUsername(String username) {
    isNotNull();
    if (!username.equals(actual.getUsername())) {
      failWithActualExpectedAndMessage(actual, username, "Expected username to be <%s> but was <%s>", username,
          actual.getUsername());
    }
    return this;
  }

  public UserAssert hasFirstName(String firstName) {
    isNotNull();
    if (!firstName.equals(actual.getFirstname())) {
      failWithActualExpectedAndMessage(actual, firstName, "Expected firstName to be <%s> but was <%s>", firstName,
          actual.getFirstname());
    }
    return this;
  }

  public UserAssert hasLastName(String lastName) {
    isNotNull();
    if (!lastName.equals(actual.getLastName())) {
      failWithActualExpectedAndMessage(actual, lastName, "Expected lastName to be <%s> but was <%s>", lastName,
          actual.getLastName());
    }
    return this;
  }

  public UserAssert hasAvatar(byte[] avatar) {
    isNotNull();
    if (!Arrays.equals(avatar, actual.getAvatar().toByteArray())) {
      failWithActualExpectedAndMessage(actual, avatar, "Expected avatar to be <%s> but was <%s>", avatar,
          actual.getAvatar());
    }
    return this;
  }

  public UserAssert hasCountryId(String countryId) {
    isNotNull();
    if (!countryId.equals(actual.getCountryId())) {
      failWithActualExpectedAndMessage(actual, countryId, "Expected countryId to be <%s> but was <%s>", countryId,
          actual.getCountryId());
    }
    return this;
  }
}
