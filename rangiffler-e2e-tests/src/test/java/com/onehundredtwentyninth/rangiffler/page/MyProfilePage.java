package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import java.nio.charset.StandardCharsets;
import org.openqa.selenium.By;

public class MyProfilePage extends BasePage<MyProfilePage> {

  private final SelenideElement pageHeader = $x("//h2[text()='My profile']");
  private final SelenideElement firstnameInput = $x("//input[@id='firstname']");
  private final SelenideElement surnameInput = $x("//input[@id='surname']");
  private final SelenideElement usernameInput = $x("//input[@id='username']");
  private final SelenideElement locationInput = $x("//div[@id='location']");
  private final SelenideElement avatar = $x("//div[contains(@class, 'MuiAvatar-root')]");
  private final SelenideElement saveButton = $x("//button[@type='submit']");
  private final SelenideElement resetButton = $x("//button[text()='Reset']");
  private final SelenideElement updateAvatarButton = $x("//span[text()='Update avatar']");
  private final SelenideElement avatarImageInput = $x("//input[@id='image__input']");

  public MyProfilePage open() {
    Selenide.open("/profile");
    return this;
  }

  public MyProfilePage pageHeaderShouldBeVisible() {
    pageHeader.shouldBe(visible);
    return this;
  }

  public MyProfilePage firstnameShouldBe(String expectedFirstname) {
    firstnameInput.shouldHave(value(expectedFirstname));
    return this;
  }

  public MyProfilePage lastnameShouldBe(String expectedLastname) {
    surnameInput.shouldHave(value(expectedLastname));
    return this;
  }

  public MyProfilePage usernameShouldBe(String expectedUsername) {
    usernameInput.shouldHave(value(expectedUsername));
    return this;
  }

  public MyProfilePage locationNameShouldBe(String expectedLocationName) {
    locationInput.shouldHave(text(expectedLocationName));
    return this;
  }

  public MyProfilePage locationFlagShouldBe(byte[] expectedLocationFlag) {
    locationInput.find(By.xpath("img"))
        .shouldHave(attribute("src", new String(expectedLocationFlag, StandardCharsets.UTF_8)));
    return this;
  }

  public MyProfilePage avatarShouldBe(byte[] expectedAvatar) {
    avatar.find(By.xpath("img"))
        .shouldHave(attribute("src", new String(expectedAvatar, StandardCharsets.UTF_8)));
    return this;
  }

  public MyProfilePage setFirstname(String firstname) {
    firstnameInput.setValue(firstname);
    return this;
  }

  public MyProfilePage setLastname(String lastname) {
    surnameInput.setValue(lastname);
    return this;
  }

  public MyProfilePage setLocation(String locationCode) {
    locationInput.click();
    $x("//li[@data-value='" + locationCode + "']").click();
    return this;
  }

  public MyProfilePage setAvatar(String fileName) {
    avatarImageInput.uploadFromClasspath(fileName);
    return this;
  }

  public MyProfilePage saveChanges() {
    saveButton.click();
    return this;
  }

  public MyProfilePage resetChanges() {
    resetButton.click();
    return this;
  }
}
