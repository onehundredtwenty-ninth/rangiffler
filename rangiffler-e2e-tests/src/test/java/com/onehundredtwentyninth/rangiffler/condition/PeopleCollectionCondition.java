package com.onehundredtwentyninth.rangiffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PeopleCollectionCondition {

  public static WebElementsCondition people(TestUser... expectedPeople) {
    return new WebElementsCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        boolean isCheckSuccess = true;
        List<TestUser> actualUsers = new ArrayList<>();

        for (var i = 0; i < elements.size(); i++) {
          var element = elements.get(i);
          var tds = element.findElements(By.xpath("td"));

          var userFromUi = TestUser.builder()
              .username(tds.get(0).getText())
              .firstname(tds.get(1).getText())
              .lastName(tds.get(2).getText())
              .build();

          actualUsers.add(userFromUi);
          var isUserTableDataMath = userFromUi.getUsername().equals(expectedPeople[i].getUsername())
              && userFromUi.getFirstname().equals(expectedPeople[i].getFirstname())
              && userFromUi.getLastName().equals(expectedPeople[i].getLastName());

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
