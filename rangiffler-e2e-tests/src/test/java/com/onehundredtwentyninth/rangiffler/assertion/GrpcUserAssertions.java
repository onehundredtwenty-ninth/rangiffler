package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.FriendStatus;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import java.util.Arrays;
import org.assertj.core.api.AbstractAssert;

public class GrpcUserAssertions extends AbstractAssert<GrpcUserAssertions, User> {

  protected GrpcUserAssertions(User user) {
    super(user, GrpcUserAssertions.class);
  }

  public static GrpcUserAssertions assertThat(User actual) {
    return new GrpcUserAssertions(actual);
  }

  public GrpcUserAssertions hasId(String id) {
    isNotNull();
    if (!id.equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public GrpcUserAssertions hasUsername(String username) {
    isNotNull();
    if (!username.equals(actual.getUsername())) {
      failWithActualExpectedAndMessage(actual, username, "Expected username to be <%s> but was <%s>", username,
          actual.getUsername());
    }
    return this;
  }

  public GrpcUserAssertions hasFirstName(String firstName) {
    isNotNull();
    if (!firstName.equals(actual.getFirstname())) {
      failWithActualExpectedAndMessage(actual, firstName, "Expected firstName to be <%s> but was <%s>", firstName,
          actual.getFirstname());
    }
    return this;
  }

  public GrpcUserAssertions hasLastName(String lastName) {
    isNotNull();
    if (!lastName.equals(actual.getLastName())) {
      failWithActualExpectedAndMessage(actual, lastName, "Expected lastName to be <%s> but was <%s>", lastName,
          actual.getLastName());
    }
    return this;
  }

  public GrpcUserAssertions hasAvatar(byte[] avatar) {
    isNotNull();
    if (!Arrays.equals(avatar == null ? new byte[]{} : avatar, actual.getAvatar().toByteArray())) {
      failWithActualExpectedAndMessage(actual, avatar, "Expected avatar to be <%s> but was <%s>", avatar,
          actual.getAvatar().toByteArray());
    }
    return this;
  }

  public GrpcUserAssertions hasCountryId(String countryId) {
    isNotNull();
    if (!countryId.equals(actual.getCountryId())) {
      failWithActualExpectedAndMessage(actual, countryId, "Expected countryId to be <%s> but was <%s>", countryId,
          actual.getCountryId());
    }
    return this;
  }

  public GrpcUserAssertions hasFriendStatus(FriendStatus friendStatus) {
    isNotNull();
    if (!friendStatus.equals(actual.getFriendStatus())) {
      failWithActualExpectedAndMessage(actual, friendStatus, "Expected friendStatus to be <%s> but was <%s>",
          friendStatus,
          actual.getFriendStatus());
    }
    return this;
  }
}
