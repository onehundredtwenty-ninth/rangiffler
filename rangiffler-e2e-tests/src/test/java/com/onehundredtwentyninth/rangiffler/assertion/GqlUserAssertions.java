package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.GqlPageInfo;
import com.onehundredtwentyninth.rangiffler.model.GqlUser;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

public class GqlUserAssertions extends AbstractAssert<GqlUserAssertions, GqlUser> {

  public GqlUserAssertions(GqlUser gqlResponse) {
    super(gqlResponse, GqlUserAssertions.class);
  }

  public static GqlUserAssertions assertThat(GqlUser actual) {
    return new GqlUserAssertions(actual);
  }

  public GqlUserAssertions hasId(UUID id) {
    isNotNull();
    if (!id.equals(actual.getId())) {
      failWithActualExpectedAndMessage(actual, id, "Expected id to be <%s> but was <%s>", id, actual.getId());
    }
    return this;
  }

  public GqlUserAssertions hasUsername(String username) {
    isNotNull();
    if (!username.equals(actual.getUsername())) {
      failWithActualExpectedAndMessage(actual, username, "Expected username to be <%s> but was <%s>", username,
          actual.getUsername());
    }
    return this;
  }

  public GqlUserAssertions hasFirstName(String firstName) {
    isNotNull();
    if (!firstName.equals(actual.getFirstname())) {
      failWithActualExpectedAndMessage(actual, firstName, "Expected firstName to be <%s> but was <%s>", firstName,
          actual.getFirstname());
    }
    return this;
  }

  public GqlUserAssertions hasLastName(String lastName) {
    isNotNull();
    if (!lastName.equals(actual.getSurname())) {
      failWithActualExpectedAndMessage(actual, lastName, "Expected surname to be <%s> but was <%s>", lastName,
          actual.getSurname());
    }
    return this;
  }

  public GqlUserAssertions hasAvatar(byte[] avatar) {
    isNotNull();
    var actualAvatar = actual.getAvatar() == null ? new byte[]{} : actual.getAvatar().getBytes(StandardCharsets.UTF_8);
    if (!Arrays.equals(avatar, actualAvatar)) {
      failWithActualExpectedAndMessage(actual, avatar, "Expected avatar to be <%s> but was <%s>", avatar,
          actual.getAvatar());
    }
    return this;
  }

  public GqlUserAssertions hasCountryCode(String countryCode) {
    isNotNull();
    if (!countryCode.equals(actual.getLocation().getCode())) {
      failWithActualExpectedAndMessage(actual, countryCode, "Expected countryId to be <%s> but was <%s>", countryCode,
          actual.getLocation().getCode());
    }
    return this;
  }

  public GqlUserAssertions hasFriendsCount(int count) {
    isNotNull();
    if (count != actual.getFriends().getEdges().size()) {
      failWithActualExpectedAndMessage(actual, count, "Expected friends count to be <%s> but was <%s>", count,
          actual.getFriends().getEdges().size());
    }
    return this;
  }

  public GqlUserAssertions hasInvitationsCount(int count) {
    isNotNull();
    if (count != actual.getIncomeInvitations().getEdges().size()) {
      failWithActualExpectedAndMessage(actual, count, "Expected invites count to be <%s> but was <%s>", count,
          actual.getIncomeInvitations().getEdges().size());
    }
    return this;
  }

  public GqlUserAssertions hasPrevious(GqlPageInfo pageInfo, boolean previous) {
    isNotNull();
    if (previous != pageInfo.hasPreviousPage()) {
      failWithActualExpectedAndMessage(actual, previous, "Expected page info previous to be <%s> but was <%s>",
          previous,
          pageInfo.hasPreviousPage()
      );
    }
    return this;
  }

  public GqlUserAssertions hasNext(GqlPageInfo pageInfo, boolean next) {
    isNotNull();
    if (next != pageInfo.hasNextPage()) {
      failWithActualExpectedAndMessage(actual, next, "Expected page info next to be <%s> but was <%s>",
          next,
          pageInfo.hasNextPage()
      );
    }
    return this;
  }
}
