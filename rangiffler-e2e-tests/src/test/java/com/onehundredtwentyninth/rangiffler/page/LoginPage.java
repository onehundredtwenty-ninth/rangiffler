package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class LoginPage extends BasePage<LoginPage> {

  private final SelenideElement loginInput = $x("//input[@name = 'username']");
  private final SelenideElement passwordInput = $x("//input[@name = 'password']");
  private final SelenideElement submitBtn = $x("//button[@type = 'submit']");

  public void login(String username, String password) {
    loginInput.setValue(username);
    passwordInput.setValue(password);
    submitBtn.click();
  }
}
