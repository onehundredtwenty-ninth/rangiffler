package com.onehundredtwentyninth.rangiffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.onehundredtwentyninth.rangiffler.model.TestCountry;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PhotoCondition {

  public static WebElementCondition exactlyPhoto(TestPhoto expectedPhoto) {
    return new WebElementCondition("Photo condition") {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        boolean isCheckSuccess = false;

        var actualPhoto = element.findElement(By.xpath("img")).getAttribute("src").getBytes(StandardCharsets.UTF_8);
        var actualDescription = element.findElement(By.xpath(".//p[contains(@class, 'photo-card__content')]"))
            .getText();
        var actualLikes = element.findElement(By.xpath(".//p[text()=' likes']")).getText();

        var countryElement = element.findElement(By.xpath(".//h3"));
        var actualCountry = new TestCountry(
            null,
            null,
            countryElement.getText(),
            countryElement.findElement(By.xpath("img")).getAttribute("src").getBytes(StandardCharsets.UTF_8)
        );

        var actualPhotoCard = TestPhoto.builder()
            .country(actualCountry)
            .description(actualDescription)
            .photo(actualPhoto)
            .build();

        var isUserTableDataMath = actualPhotoCard.getDescription().equals(expectedPhoto.getDescription())
            && actualPhotoCard.getCountry().getName().equals(expectedPhoto.getCountry().getName())
            && Arrays.equals(actualPhotoCard.getCountry().getFlag(), expectedPhoto.getCountry().getFlag())
            && Arrays.equals(actualPhotoCard.getPhoto(), expectedPhoto.getPhoto())
            && Integer.parseInt(actualLikes.split(" ")[0]) == expectedPhoto.getLikes().size();

        if (isUserTableDataMath) {
          isCheckSuccess = true;
        }

        if (isCheckSuccess) {
          return CheckResult.accepted();
        } else {
          var errorMsg = String.format("Incorrect photo content. Expected photo: %s, actual photo list: %s",
              expectedPhoto, actualPhoto);
          return CheckResult.rejected(errorMsg, element);
        }
      }

      @Override
      public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
      }
    };
  }

  public static WebElementCondition photoWithoutLikes(TestPhoto expectedPhoto) {
    return new WebElementCondition("Photo condition") {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        boolean isCheckSuccess = false;

        var actualPhoto = element.findElement(By.xpath("img")).getAttribute("src").getBytes(StandardCharsets.UTF_8);
        var actualDescription = element.findElement(By.xpath(".//p[contains(@class, 'photo-card__content')]"))
            .getText();

        var countryElement = element.findElement(By.xpath(".//h3"));
        var actualCountry = new TestCountry(
            null,
            null,
            countryElement.getText(),
            countryElement.findElement(By.xpath("img")).getAttribute("src").getBytes(StandardCharsets.UTF_8)
        );

        var actualPhotoCard = TestPhoto.builder()
            .country(actualCountry)
            .description(actualDescription)
            .photo(actualPhoto)
            .build();

        var isUserTableDataMath = actualPhotoCard.getDescription().equals(expectedPhoto.getDescription())
            && actualPhotoCard.getCountry().getName().equals(expectedPhoto.getCountry().getName())
            && Arrays.equals(actualPhotoCard.getCountry().getFlag(), expectedPhoto.getCountry().getFlag())
            && Arrays.equals(actualPhotoCard.getPhoto(), expectedPhoto.getPhoto());
        if (isUserTableDataMath) {
          isCheckSuccess = true;
        }

        if (isCheckSuccess) {
          return CheckResult.accepted();
        } else {
          var errorMsg = String.format("Incorrect photo content. Expected photo: %s, actual photo list: %s",
              expectedPhoto, actualPhoto);
          return CheckResult.rejected(errorMsg, element);
        }
      }

      @Override
      public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
      }
    };
  }
}
