package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class StartPage extends BasePage<StartPage> {

  private final SelenideElement loginBtn = $x("//button[text() = 'Login']");
  private final SelenideElement registerBtn = $x("//a[text() = 'Register']");

  public StartPage open() {
    Selenide.open("/");
    return this;
  }

  public void clickLoginBtn() {
    loginBtn.click();
  }

  public void clickRegisterBtn() {
    registerBtn.click();
  }
}
