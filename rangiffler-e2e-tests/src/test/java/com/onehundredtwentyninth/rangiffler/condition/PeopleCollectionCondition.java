package com.onehundredtwentyninth.rangiffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.onehundredtwentyninth.rangiffler.model.TestCountry;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PeopleCollectionCondition {

  public static WebElementsCondition peopleExactly(TestUser... expectedPeople) {
    return new WebElementsCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        boolean isCheckSuccess = true;
        List<TestUser> actualUsers = new ArrayList<>();

        for (var i = 0; i < elements.size(); i++) {
          var element = elements.get(i);
          var tds = element.findElements(By.xpath("td"));
          var avatar = element.findElement(By.xpath("th//img")).getAttribute("src").getBytes(StandardCharsets.UTF_8);

          var userFromUi = TestUser.builder()
              .username(tds.get(0).getText())
              .firstname(tds.get(1).getText())
              .lastName(tds.get(2).getText())
              .country(new TestCountry(null, null, tds.get(3).getText(), null))
              .avatar(avatar)
              .build();

          actualUsers.add(userFromUi);
          var isUserTableDataMath = userFromUi.getUsername().equals(expectedPeople[i].getUsername())
              && userFromUi.getFirstname().equals(expectedPeople[i].getFirstname())
              && userFromUi.getLastName().equals(expectedPeople[i].getLastName())
              && userFromUi.getCountry().getName().equals(expectedPeople[i].getCountry().getName())
              && Arrays.equals(userFromUi.getAvatar(), expectedPeople[i].getAvatar());

          if (!isUserTableDataMath) {
            isCheckSuccess = false;
          }
        }

        if (isCheckSuccess) {
          return CheckResult.accepted();
        } else {
          var errorMsg = String.format("Incorrect users content. Expected user list: %s, actual user list: %s",
              Arrays.toString(expectedPeople), actualUsers);
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
