package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;

public class MyTravelsPage extends BasePage<MyTravelsPage> {

  private final SelenideElement travelsMapHeader = $x("//h2[text() = 'Travels map']");

  public MyTravelsPage travelsMapHeaderShouldBeVisible() {
    travelsMapHeader.shouldBe(visible);
    return this;
  }
}
