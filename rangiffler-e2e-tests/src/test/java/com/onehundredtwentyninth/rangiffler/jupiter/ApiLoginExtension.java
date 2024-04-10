package com.onehundredtwentyninth.rangiffler.jupiter;

import com.onehundredtwentyninth.rangiffler.api.cookie.ThreadSafeCookieManager;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.model.TokenResponse;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
  private static final Config CFG = Config.getInstance();

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {

  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    ThreadSafeCookieManager.INSTANCE.removeAll();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return false;
  }

  @Override
  public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return "Bearer " + getTokens(extensionContext).idToken();
  }

  public static TokenResponse getTokens(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("tokens", TokenResponse.class);
  }
}
