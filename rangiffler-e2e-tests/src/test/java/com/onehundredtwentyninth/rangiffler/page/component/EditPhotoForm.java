package com.onehundredtwentyninth.rangiffler.page.component;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.SelenideElement;
import java.nio.charset.StandardCharsets;

public class EditPhotoForm extends BaseComponent<EditPhotoForm> {

  private final SelenideElement photo = self.$x(".//img");
  private final SelenideElement locationInput = self.$x(".//div[@id='country']");
  private final SelenideElement descriptionInput = self.$x(".//textarea[@id='description']");
  private final SelenideElement saveButton = self.$x(".//button[@type='submit']");

  public EditPhotoForm(SelenideElement self) {
    super(self);
  }

  public EditPhotoForm imageShouldBePresented(byte[] expectedPhoto) {
    photo.shouldHave(attribute("src", new String(expectedPhoto, StandardCharsets.UTF_8)));
    return this;
  }

  public EditPhotoForm editPhoto(String countryCode, String description) {
    locationInput.click();
    $x("//li[@data-value='" + countryCode + "']").click();
    descriptionInput.setValue(description);
    saveButton.click();
    return this;
  }
}
