package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class RegisterPage extends BasePage<RegisterPage> {

  private final SelenideElement usernameInput = $x("//input[@id='username']");
  private final SelenideElement passwordInput = $x("//input[@id='password']");
  private final SelenideElement passwordSubmitInput = $x("//input[@id='passwordSubmit']");
  private final SelenideElement submitButton = $x("//button[@type='submit']");
  private final SelenideElement successRegisterMessage = $x("//p[text()=\"Congratulations! You've registered!\"]");

  public RegisterPage open() {
    Selenide.open("/register");
    return this;
  }

  public RegisterPage fillRegisterPage(String login, String password) {
    setUsername(login);
    setPassword(password);
    setPasswordSubmit(password);
    return this;
  }

  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    setUsername(login);
    setPassword(password);
    setPasswordSubmit(passwordSubmit);
    return this;
  }

  public RegisterPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  public RegisterPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  public RegisterPage setPasswordSubmit(String password) {
    passwordSubmitInput.setValue(password);
    return this;
  }

  public void successSubmit() {
    submitButton.click();
    successRegisterMessage.shouldBe(visible);
  }
}
