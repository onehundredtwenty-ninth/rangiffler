package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

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

  @Step("Fill register page with credentials: username: {login}, password: {password}")
  public RegisterPage fillRegisterPage(String login, String password) {
    setUsername(login);
    setPassword(password);
    setPasswordSubmit(password);
    return this;
  }

  @Step("Fill register page with credentials: username: {login}, password: {password}, submit password: {passwordSubmit}")
  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    setUsername(login);
    setPassword(password);
    setPasswordSubmit(passwordSubmit);
    return this;
  }

  @Step("Set username: {0}")
  public RegisterPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  @Step("Set password: {0}")
  public RegisterPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Confirm password: {0}")
  public RegisterPage setPasswordSubmit(String password) {
    passwordSubmitInput.setValue(password);
    return this;
  }

  @Step("Submit register")
  public void successSubmit() {
    submitButton.click();
    successRegisterMessage.shouldBe(visible);
  }
}
