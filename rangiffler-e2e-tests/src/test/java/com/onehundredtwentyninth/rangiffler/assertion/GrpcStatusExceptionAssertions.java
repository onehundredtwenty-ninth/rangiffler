package com.onehundredtwentyninth.rangiffler.assertion;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import io.grpc.StatusRuntimeException;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

public class GrpcStatusExceptionAssertions extends AbstractThrowableAssert<GrpcStatusExceptionAssertions, Throwable> {

  public GrpcStatusExceptionAssertions(Throwable statusRuntimeException) {
    super(statusRuntimeException, GrpcStatusExceptionAssertions.class);
  }

  public static GrpcStatusExceptionAssertions assertThat(Throwable actual) {
    return new GrpcStatusExceptionAssertions(actual);
  }

  public static GrpcStatusExceptionAssertions assertThatThrownBy(ThrowingCallable shouldRaiseThrowable) {
    return assertThat(catchThrowable(shouldRaiseThrowable)).hasBeenThrown();
  }

  public GrpcStatusExceptionAssertions isInstanceOfStatusRuntimeException() {
    objects.assertIsInstanceOf(info, actual, StatusRuntimeException.class);
    return this;
  }

  public GrpcStatusExceptionAssertions hasUserNotFoundMessage(String userAttribute) {
    isNotNull();
    var expectedMessage = "NOT_FOUND: User " + userAttribute + " not found";
    if (!expectedMessage.equals(actual.getMessage())) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message to be <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }

  public GrpcStatusExceptionAssertions hasCountryNotFoundMessage(String countryAttribute) {
    isNotNull();
    var expectedMessage = "NOT_FOUND: Country " + countryAttribute + " not found";
    if (!expectedMessage.equals(actual.getMessage())) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message to be <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }

  public GrpcStatusExceptionAssertions hasInvalidIdMessage(String id) {
    isNotNull();
    var expectedMessage = "ABORTED: Invalid UUID string: " + id;
    if (!expectedMessage.equals(actual.getMessage())) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message to be <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }

  public GrpcStatusExceptionAssertions hasFriendshipNotFoundMessage(String firstUsername, String secondUsername) {
    isNotNull();
    var expectedMessage = "NOT_FOUND: Friendship between " + firstUsername + " and " + secondUsername + " not found";
    if (!expectedMessage.equals(actual.getMessage())) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message to be <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }

  public GrpcStatusExceptionAssertions hasFriendshipRequestNotFoundMessage(String firstUsername,
      String secondUsername) {
    isNotNull();
    var expectedMessage =
        "NOT_FOUND: Friendship request from " + firstUsername + " to " + secondUsername + " not found";
    if (!expectedMessage.equals(actual.getMessage())) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message to be <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }

  public GrpcStatusExceptionAssertions hasAbortedMessage() {
    isNotNull();
    var expectedMessage = "ABORTED";
    if (!expectedMessage.equals(actual.getMessage())) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message to be <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }

  public GrpcStatusExceptionAssertions messageContains(String expectedMessage) {
    isNotNull();
    if (!actual.getMessage().contains(expectedMessage)) {
      failWithActualExpectedAndMessage(actual, expectedMessage, "Expected message contain <%s> but was <%s>",
          expectedMessage,
          actual.getMessage()
      );
    }
    return this;
  }
}
