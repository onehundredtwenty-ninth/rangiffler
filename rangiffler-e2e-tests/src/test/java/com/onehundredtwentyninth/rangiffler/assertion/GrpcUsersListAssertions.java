package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import org.assertj.core.api.AbstractAssert;

public class GrpcUsersListAssertions extends AbstractAssert<GrpcUsersListAssertions, AllUsersResponse> {

  protected GrpcUsersListAssertions(AllUsersResponse allUsersResponse) {
    super(allUsersResponse, GrpcUsersListAssertions.class);
  }

  public static GrpcUsersListAssertions assertThat(AllUsersResponse actual) {
    return new GrpcUsersListAssertions(actual);
  }

  public GrpcUsersListAssertions hasPageSize(Integer pageSize) {
    isNotNull();
    if (pageSize != actual.getAllUsersCount()) {
      failWithActualExpectedAndMessage(actual, pageSize, "Expected page size to be <%s> but was <%s>", pageSize,
          actual.getAllUsersCount());
    }
    return this;
  }

  public GrpcUsersListAssertions hasNext(boolean hasNext) {
    isNotNull();
    if (hasNext != actual.getHasNext()) {
      failWithActualExpectedAndMessage(actual, hasNext, "Expected hasNext to be <%s> but was <%s>", hasNext,
          actual.getHasNext());
    }
    return this;
  }

  public GrpcUsersListAssertions hasUserWithUsername(String username) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> username.equals(s.getUsername()));
    if (!isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, username,
          "Expected response to have user with username <%s> but it's not", username);
    }
    return this;
  }

  public GrpcUsersListAssertions hasUserWithFirstName(String firstname) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> firstname.equals(s.getFirstname()));
    if (!isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, firstname,
          "Expected response to have user with firstname <%s> but it's not", firstname);
    }
    return this;
  }

  public GrpcUsersListAssertions hasUserWithLastName(String lastName) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> lastName.equals(s.getLastName()));
    if (!isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, lastName,
          "Expected response to have user with lastName <%s> but it's not", lastName);
    }
    return this;
  }

  public GrpcUsersListAssertions hasNotUserWithUsername(String username) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> username.equals(s.getUsername()));
    if (isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, username,
          "Expected response to have not user with username <%s> but it was", username);
    }
    return this;
  }

  public GrpcUsersListAssertions containsUser(User user) {
    isNotNull();
    boolean isUserPresented = actual.getAllUsersList().stream().anyMatch(user::equals);
    if (!isUserPresented) {
      failWithActualExpectedAndMessage(actual, user,
          "Expected response to have user <%s> but it's not", user);
    }
    return this;
  }

  public GrpcUsersListAssertions notContainsUser(User user) {
    isNotNull();
    boolean isUserPresented = actual.getAllUsersList().stream().anyMatch(user::equals);
    if (isUserPresented) {
      failWithActualExpectedAndMessage(actual, user,
          "Expected response to have not user <%s> but it was", user);
    }
    return this;
  }

  public GrpcUsersListAssertions notContainsUserWithName(String username) {
    isNotNull();
    boolean isUserPresented = actual.getAllUsersList().stream().anyMatch(s -> s.getUsername().equals(username));
    if (isUserPresented) {
      failWithActualExpectedAndMessage(actual, username,
          "Expected response to have not user <%s> but it was", username);
    }
    return this;
  }
}
