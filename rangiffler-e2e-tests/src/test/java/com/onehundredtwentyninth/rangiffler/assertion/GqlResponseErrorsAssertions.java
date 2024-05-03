package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.model.gql.GqlError;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

public class GqlResponseErrorsAssertions extends AbstractAssert<GqlResponseErrorsAssertions, GqlError> {

  public GqlResponseErrorsAssertions(GqlError gqlError) {
    super(gqlError, GqlResponseErrorsAssertions.class);
  }

  public static GqlResponseErrorsAssertions assertThat(GqlError actual) {
    return new GqlResponseErrorsAssertions(actual);
  }

  public GqlResponseErrorsAssertions hasMessage(String expectedMessage) {
    isNotNull();
    if (!expectedMessage.equals(actual.message())) {
      failWithActualExpectedAndMessage(actual, expectedMessage,
          "Expected error message to be <%s> but was <%s>",
          expectedMessage,
          actual.message()
      );
    }
    return this;
  }

  public GqlResponseErrorsAssertions messageContains(String expectedMessage) {
    isNotNull();
    if (!actual.message().contains(expectedMessage)) {
      failWithActualExpectedAndMessage(actual, expectedMessage,
          "Expected error message to contain <%s> but was <%s>",
          expectedMessage,
          actual.message()
      );
    }
    return this;
  }

  public GqlResponseErrorsAssertions hasPath(List<String> expectedPath) {
    isNotNull();
    if (!expectedPath.equals(actual.path())) {
      failWithActualExpectedAndMessage(actual, expectedPath,
          "Expected error message to be <%s> but was <%s>",
          expectedPath,
          actual.path()
      );
    }
    return this;
  }

  public GqlResponseErrorsAssertions hasExtensions(Map<String, String> expectedExtensions) {
    isNotNull();
    if (!expectedExtensions.equals(actual.extensions())) {
      failWithActualExpectedAndMessage(actual, expectedExtensions,
          "Expected error message to be <%s> but was <%s>",
          expectedExtensions,
          actual.extensions()
      );
    }
    return this;
  }

  public GqlResponseErrorsAssertions hasPhotoPermissionDeniedMessage(UUID photoId, UUID userId) {
    return hasMessage("PERMISSION_DENIED: Photo with id " + photoId + " can't be modified by user " + userId);
  }

  public GqlResponseErrorsAssertions hasPhotoNotFoundMessage(UUID photoId) {
    return hasMessage("NOT_FOUND: Photo with id " + photoId + " not found");
  }

  public GqlResponseErrorsAssertions hasFriendshipNotFoundMessage(String username, String friendUsername) {
    return hasMessage("NOT_FOUND: Friendship between " + username + " and " + friendUsername + " not found");
  }

  public GqlResponseErrorsAssertions hasFriendshipRequestNotFoundMessage(String username, String friendUsername) {
    return hasMessage("NOT_FOUND: Friendship request from " + username + " to " + friendUsername + " not found");
  }

  public GqlResponseErrorsAssertions hasUserNotFoundMessage(UUID photoId) {
    return hasMessage("NOT_FOUND: User " + photoId + " not found");
  }

  public GqlResponseErrorsAssertions hasInternalErrorExtension() {
    return hasExtensions(Map.of("classification", "INTERNAL_ERROR"));
  }
}
