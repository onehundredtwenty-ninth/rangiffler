package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.github.javafaker.Faker;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Extras;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.service.UserDbService;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

@Slf4j
public class CreateExtrasUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(CreateExtrasUserExtension.class);
  private final UserService userService = new UserDbService();
  private final Faker faker = new Faker();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var usersParameters = extractUsersForTest(extensionContext);

    List<TestUser> createdUsers = new ArrayList<>();
    for (var userParameters : usersParameters) {
      var createdUser = userService.createRandomUser();
      createdUsers.add(createdUser);
    }
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createdUsers);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void afterEach(ExtensionContext extensionContext) {
    List<TestUser> createdUsers = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class);
    for (var createdUser : createdUsers) {
      userService.deleteUser(createdUser.getUsername());
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return (parameterContext.getParameter().getType().isAssignableFrom(TestUser[].class)
        && extensionContext.getRequiredTestMethod().isAnnotationPresent(CreateExtrasUsers.class)
        && parameterContext.getParameter().isAnnotationPresent(Extras.class));
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
        .toArray(TestUser[]::new);
  }

  private List<CreateUser> extractUsersForTest(ExtensionContext context) {
    List<CreateUser> users = new ArrayList<>();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), CreateExtrasUsers.class).ifPresent(
        createUsers -> users.addAll(Arrays.asList(createUsers.value()))
    );
    return users;
  }
}
