package com.onehundredtwentyninth.rangiffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.onehundredtwentyninth.rangiffler.model.TestCountry;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PhotoCollectionCondition {

  public static WebElementsCondition photosExactly(TestPhoto... expectedPhotos) {
    return new WebElementsCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedPhotos.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }

        boolean isCheckSuccess = true;
        List<TestPhoto> actualPhotos = new ArrayList<>();

        for (var i = 0; i < elements.size(); i++) {
          var element = elements.get(i);
          var actualPhoto = element.findElement(By.xpath("img")).getAttribute("src").getBytes(StandardCharsets.UTF_8);
          var actualDescription = element.findElement(By.xpath("//p[contains(@class, 'photo-card__content')]")).getText();
          var actualLikes = element.findElement(By.xpath("//p[text()=' likes']")).getText();

          var countryElement = element.findElement(By.xpath("//h3"));
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

          actualPhotos.add(actualPhotoCard);
          var isUserTableDataMath = actualPhotoCard.getDescription().equals(expectedPhotos[i].getDescription())
              && actualPhotoCard.getCountry().getName().equals(expectedPhotos[i].getCountry().getName())
              && Arrays.equals(actualPhotoCard.getCountry().getFlag(), expectedPhotos[i].getCountry().getFlag())
              && Arrays.equals(actualPhotoCard.getPhoto(), expectedPhotos[i].getPhoto())
              && Integer.parseInt(actualLikes.split(" ")[0]) == expectedPhotos[i].getLikes().size();

          if (!isUserTableDataMath) {
            isCheckSuccess = false;
          }
        }

        if (isCheckSuccess) {
          return CheckResult.accepted();
        } else {
          var errorMsg = String.format("Incorrect users content. Expected user list: %s, actual user list: %s",
              Arrays.toString(expectedPhotos), actualPhotos);
          return CheckResult.rejected(errorMsg, elements);
        }
      }

      @Override
      public String toString() {
        return null;
      }
    };
  }
}
