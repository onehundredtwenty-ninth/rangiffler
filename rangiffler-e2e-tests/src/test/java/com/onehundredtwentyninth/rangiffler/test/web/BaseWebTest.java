package com.onehundredtwentyninth.rangiffler.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.ApiLoginExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.BrowserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.CreateExtrasUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.CreateUserExtension;
import com.onehundredtwentyninth.rangiffler.jupiter.extension.GuiceExtension;
import io.qameta.allure.junit5.AllureJunit5;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({AllureJunit5.class, GuiceExtension.class, CreateUserExtension.class, CreateExtrasUserExtension.class,
    ApiLoginExtension.class, BrowserExtension.class})
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
