package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.onehundredtwentyninth.rangiffler.guicebinding.BasicModule;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class GuiceExtension implements TestInstancePostProcessor {

  private final Injector injector = Guice.createInjector(new BasicModule());

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
    injector.injectMembers(testInstance);
  }
}
