package com.onehundredtwentyninth.rangiffler.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.onehundredtwentyninth.rangiffler.jupiter.BrowserExtension;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({AllureJunit5.class, BrowserExtension.class})
public abstract class BaseWebTest {

  @BeforeEach
  void addAllureSelenideListener() {
    SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
        .screenshots(true)
        .savePageSource(true)
        .includeSelenideSteps(false)
    );
  }
}
