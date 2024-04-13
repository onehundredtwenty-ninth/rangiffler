package com.onehundredtwentyninth.rangiffler.jupiter;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.onehundredtwentyninth.rangiffler.guicebinding.BasicModule;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class GuiceExtension implements TestInstancePostProcessor, ParameterResolver {

  private final Injector injector = Guice.createInjector(new BasicModule());

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
    injector.injectMembers(testInstance);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().isAnnotationPresent(Inject.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return injector.getInstance(parameterContext.getParameter().getType());
  }
}
