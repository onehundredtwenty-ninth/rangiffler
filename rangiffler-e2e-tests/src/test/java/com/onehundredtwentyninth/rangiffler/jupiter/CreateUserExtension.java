package com.onehundredtwentyninth.rangiffler.jupiter;

import com.github.javafaker.Faker;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.service.UserDbService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

@Slf4j
public class CreateUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(CreateUserExtension.class);
  private final UserDbService userDbService = new UserDbService();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var userParameters = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        CreateUser.class
    );

    if (userParameters.isPresent()) {
      String username = userParameters.get().username().isEmpty()
          ? new Faker().name().username()
          : userParameters.get().username();
      String password = userParameters.get().password().isEmpty()
          ? "123"
          : userParameters.get().password();

      var createdUser = userDbService.createUser(username, password);
      extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createdUser);
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    var createdUser = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), User.class);
    if (createdUser != null) {
      userDbService.deleteUser(createdUser);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(User.class)
        && extensionContext.getRequiredTestMethod().isAnnotationPresent(CreateUser.class);
  }

  @Override
  public User resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), User.class);
  }
}
