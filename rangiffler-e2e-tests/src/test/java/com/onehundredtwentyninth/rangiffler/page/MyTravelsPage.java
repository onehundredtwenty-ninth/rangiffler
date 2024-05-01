package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import com.onehundredtwentyninth.rangiffler.page.component.PhotoCardsBar;
import com.onehundredtwentyninth.rangiffler.page.component.WorldMap;

public class MyTravelsPage extends BasePage<MyTravelsPage> {

  private final SelenideElement travelsMapHeader = $x("//h2[text() = 'Travels map']");
  private final SelenideElement onlyMyTravelsButton = $x("//button[text()='Only my travels']");
  private final SelenideElement withFriendsButton = $x("//button[text()='With friends']");
  private final WorldMap worldMap = new WorldMap($x("//figure[@class='worldmap__figure-container']"));
  private final SelenideElement addPhotoButton = $x("//button[text()='Add photo']");
  private final PhotoCardsBar photoCardsBar = new PhotoCardsBar($x("//div[contains(@class,'MuiGrid-root MuiGrid-container')]"));

  public MyTravelsPage open() {
    Selenide.open("/my-travels");
    return this;
  }

  public MyTravelsPage travelsMapHeaderShouldBeVisible() {
    travelsMapHeader.shouldBe(visible);
    return this;
  }

  public MyTravelsPage clickWithFriendsButton() {
    withFriendsButton.click();
    return this;
  }

  public MyTravelsPage exactlyPhotoCardsShouldBePresented(TestPhoto... expectedPhotos) {
    photoCardsBar.exactlyPhotoCardsShouldBePresented(expectedPhotos);
    return this;
  }

  public MyTravelsPage photoCardsShouldBePresented(TestPhoto... expectedPhotos) {
    photoCardsBar.photoCardsShouldBePresented(expectedPhotos);
    return this;
  }

  public MyTravelsPage photosCountShouldBeEqualTo(int expectedCount) {
    photoCardsBar.photosCountShouldBeEqualTo(expectedCount);
    return this;
  }

  public MyTravelsPage statisticShouldBePresented(String countryCode, String countryName, int expectedCount) {
    worldMap.statisticShouldBePresented(countryCode, countryName, expectedCount);
    return this;
  }
}
