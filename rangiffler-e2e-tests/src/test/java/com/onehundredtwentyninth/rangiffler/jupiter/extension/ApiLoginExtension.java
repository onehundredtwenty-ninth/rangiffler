package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.onehundredtwentyninth.rangiffler.api.cookie.ThreadSafeCookieManager;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.ApiLogin;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.Token;
import com.onehundredtwentyninth.rangiffler.model.Credentials;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.model.TokenResponse;
import com.onehundredtwentyninth.rangiffler.service.AuthService;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
  private final AuthService authService = new AuthService();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var apiLoginAnnotation = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        ApiLogin.class
    );

    var createUserAnnotation = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        CreateUser.class
    ).orElse(null);

    if (apiLoginAnnotation.isPresent()) {
      var credentials = findUserCredentials(apiLoginAnnotation.get(), createUserAnnotation, extensionContext);
      var tokens = authService.doLogin(credentials.login(), credentials.password());
      setTokens(extensionContext, tokens);
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) {
    ThreadSafeCookieManager.INSTANCE.removeAll();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(Token.class)
        && parameterContext.getParameter().getType().isAssignableFrom(String.class);
  }

  @Override
  public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return getTokens(extensionContext).idToken();
  }

  public static void setTokens(ExtensionContext context, TokenResponse tokens) {
    context.getStore(NAMESPACE).put(context.getUniqueId() + "tokens", tokens);
  }

  public static TokenResponse getTokens(ExtensionContext context) {
    return context.getStore(NAMESPACE).get(context.getUniqueId() + "tokens", TokenResponse.class);
  }

  private Credentials findUserCredentials(ApiLogin apiLoginParameters, CreateUser createUserAnnotation,
      ExtensionContext extensionContext) {
    if (!apiLoginParameters.username().isBlank() && !apiLoginParameters.password().isBlank()) {
      return new Credentials(apiLoginParameters.username(), apiLoginParameters.password());
    }

    if (createUserAnnotation != null) {
      var user = extensionContext.getStore(CreateUserExtension.NAMESPACE)
          .get(extensionContext.getUniqueId(), TestUser.class);
      return new Credentials(user.getUsername(), user.getTestData().password());
    }

    throw new IllegalStateException("Не найдены учетные данные для ApiLoginExtension");
  }
}