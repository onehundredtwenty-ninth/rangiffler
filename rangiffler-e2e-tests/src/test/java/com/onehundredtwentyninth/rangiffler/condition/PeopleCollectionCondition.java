package com.onehundredtwentyninth.rangiffler.condition;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.codeborne.selenide.impl.CollectionSource;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import com.onehundredtwentyninth.rangiffler.page.component.UserCard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.openqa.selenium.WebElement;

public class PeopleCollectionCondition {

  public static WebElementsCondition peopleExactly(TestUser... expectedPeople) {
    return new WebElementsCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedPeople.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }

        boolean isCheckSuccess = true;
        List<TestUser> actualUsers = new ArrayList<>();

        for (var i = 0; i < elements.size(); i++) {
          var userCard = new UserCard($(elements.get(i)));
          actualUsers.add(userCard.toTestUser());
          var isUserTableDataMath = userCard.equalTo(expectedPeople[i]);

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
      public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause,
          long timeoutMs) {
        throw new AssertionError(lastCheckResult.message());
      }

      @Override
      public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
      }
    };
  }

  public static WebElementsCondition containsUser(TestUser expectedUser) {
    return new WebElementsCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        List<TestUser> actualPhotos = new ArrayList<>();

        for (var element : elements) {
          final var userCard = new UserCard($(element));
          actualPhotos.add(userCard.toTestUser());

          var isUserTableDataMath = userCard.equalTo(expectedUser);
          if (isUserTableDataMath) {
            return CheckResult.accepted();
          }
        }

        var errorMsg = String.format("Incorrect user content. Expected user: %s, actual user list: %s",
            expectedUser, actualPhotos);
        return CheckResult.rejected(errorMsg, elements);
      }

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause,
          long timeoutMs) {
        throw new AssertionError(lastCheckResult.message());
      }

      @Override
      public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
      }
    };
  }
}
