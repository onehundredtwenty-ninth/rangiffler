package com.onehundredtwentyninth.rangiffler.page.component;

import static com.codeborne.selenide.Selenide.$$x;
import static com.onehundredtwentyninth.rangiffler.condition.PhotoCollectionCondition.photosExactly;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;

public class PhotoCardsBar extends BaseComponent<PhotoCardsBar> {

  private final ElementsCollection photoCards = $$x("//div[contains(@class, 'photo-card__container')]");

  public PhotoCardsBar(SelenideElement self) {
    super(self);
  }

  public PhotoCardsBar exactlyPhotoCardsShouldBePresented(TestPhoto... expectedPhotos) {
    photoCards.shouldHave(photosExactly(expectedPhotos));
    return this;
  }
}
