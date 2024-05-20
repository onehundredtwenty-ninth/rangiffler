package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.onehundredtwentyninth.rangiffler.config.Config;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BrowserExtension implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    Selenide.open(Config.getInstance().frontUrl());

    var tokens = ApiLoginExtension.getTokens(extensionContext);
    if (tokens != null) {
      var localStorage = Selenide.localStorage();
      localStorage.setItem("id_token", tokens.idToken());
      Selenide.refresh();
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Selenide.closeWebDriver();
    }
  }
}
