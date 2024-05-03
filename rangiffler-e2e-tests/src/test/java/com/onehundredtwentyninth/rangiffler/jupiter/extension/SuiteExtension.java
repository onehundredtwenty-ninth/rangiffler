package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

public interface SuiteExtension extends BeforeAllCallback {

  @Override
  default void beforeAll(ExtensionContext extensionContext) throws Exception {
    extensionContext.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
        .getOrComputeIfAbsent(this.getClass(), key -> {
          beforeSuite(extensionContext);
          return (CloseableResource) this::afterSuite;
        });
  }

  default void beforeSuite(ExtensionContext extensionContext) {

  }

  default void afterSuite() {

  }
}
