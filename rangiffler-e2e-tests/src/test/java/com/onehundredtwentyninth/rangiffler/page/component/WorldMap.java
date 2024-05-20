package com.onehundredtwentyninth.rangiffler.page.component;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.ownText;
import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class WorldMap extends BaseComponent<WorldMap> {

  public WorldMap(SelenideElement self) {
    super(self);
  }

  public WorldMap statisticShouldBePresented(String countryCode, String countryName, int expectedCount) {
    var actualStatistic = self.find(By.xpath(".//*[contains(text(),'" + countryName + "')]"));
    var countryElement = self.find(By.xpath(".//*[@id='" + countryCode.toUpperCase() + "']"));

    actualStatistic
        .shouldBe(exist)
        .shouldNotBe(visible)
        .shouldHave(ownText(countryName + " " + expectedCount));

    countryElement.hover();
    actualStatistic.shouldBe(visible);

    return this;
  }
}
