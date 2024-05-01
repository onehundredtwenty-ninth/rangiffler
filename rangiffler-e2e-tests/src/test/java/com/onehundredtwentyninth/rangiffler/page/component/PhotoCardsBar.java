package com.onehundredtwentyninth.rangiffler.page.component;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$$x;
import static com.onehundredtwentyninth.rangiffler.condition.PhotoCollectionCondition.containsPhoto;
import static com.onehundredtwentyninth.rangiffler.condition.PhotoCollectionCondition.photosExactly;
import static com.onehundredtwentyninth.rangiffler.condition.PhotoCondition.exactlyPhoto;

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

  public PhotoCardsBar photoCardsShouldBePresented(TestPhoto... expectedPhotos) {
    for (var expectedPhoto : expectedPhotos) {
      photoCards.should(containsPhoto(expectedPhoto));
    }
    return this;
  }

  public PhotoCardsBar photosCountShouldBeEqualTo(int expectedCount) {
    photoCards.shouldHave(size(expectedCount));
    return this;
  }

  public SelenideElement getPhotoCard(TestPhoto photo) {
    return photoCards.filter(exactlyPhoto(photo)).get(0);
  }
}
