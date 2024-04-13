package com.onehundredtwentyninth.rangiffler.jupiter;

import com.codeborne.selenide.Selenide;
import com.onehundredtwentyninth.rangiffler.config.Config;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BrowserLoginExtension implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var tokens = ApiLoginExtension.getTokens(extensionContext);
    Selenide.open(Config.getInstance().frontUrl());
    var localStorage = Selenide.localStorage();
    localStorage.setItem("id_token", tokens.idToken());
    Selenide.refresh();
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    Selenide.localStorage().clear();
  }
}
