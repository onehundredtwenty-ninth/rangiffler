package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.grpc.AllUsersResponse;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import org.assertj.core.api.AbstractAssert;

public class AllUsersAssert extends AbstractAssert<AllUsersAssert, AllUsersResponse> {

  protected AllUsersAssert(AllUsersResponse allUsersResponse) {
    super(allUsersResponse, AllUsersAssert.class);
  }

  public static AllUsersAssert assertThat(AllUsersResponse actual) {
    return new AllUsersAssert(actual);
  }

  public AllUsersAssert hasPageSize(Integer pageSize) {
    isNotNull();
    if (pageSize != actual.getAllUsersCount()) {
      failWithActualExpectedAndMessage(actual, pageSize, "Expected page size to be <%s> but was <%s>", pageSize,
          actual.getAllUsersCount());
    }
    return this;
  }

  public AllUsersAssert hasNext(boolean hasNext) {
    isNotNull();
    if (hasNext != actual.getHasNext()) {
      failWithActualExpectedAndMessage(actual, hasNext, "Expected hasNext to be <%s> but was <%s>", hasNext,
          actual.getHasNext());
    }
    return this;
  }

  public AllUsersAssert hasUserWithUsername(String username) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> username.equals(s.getUsername()));
    if (!isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, username,
          "Expected response to have user with username <%s> but it's not", username);
    }
    return this;
  }

  public AllUsersAssert hasUserWithFirstName(String firstname) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> firstname.equals(s.getFirstname()));
    if (!isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, firstname,
          "Expected response to have user with firstname <%s> but it's not", firstname);
    }
    return this;
  }

  public AllUsersAssert hasUserWithLastName(String lastName) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> lastName.equals(s.getLastName()));
    if (!isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, lastName,
          "Expected response to have user with lastName <%s> but it's not", lastName);
    }
    return this;
  }

  public AllUsersAssert hasNotUserWithUsername(String username) {
    isNotNull();
    boolean isUserWithUsernamePresented = actual.getAllUsersList().stream()
        .anyMatch(s -> username.equals(s.getUsername()));
    if (isUserWithUsernamePresented) {
      failWithActualExpectedAndMessage(actual, username,
          "Expected response to have not user with username <%s> but it was", username);
    }
    return this;
  }

  public AllUsersAssert containsUser(User user) {
    isNotNull();
    boolean isUserPresented = actual.getAllUsersList().stream().anyMatch(user::equals);
    if (!isUserPresented) {
      failWithActualExpectedAndMessage(actual, user,
          "Expected response to have user <%s> but it's not", user);
    }
    return this;
  }

  public AllUsersAssert notContainsUser(User user) {
    isNotNull();
    boolean isUserPresented = actual.getAllUsersList().stream().anyMatch(user::equals);
    if (isUserPresented) {
      failWithActualExpectedAndMessage(actual, user,
          "Expected response to have not user <%s> but it was", user);
    }
    return this;
  }
}
