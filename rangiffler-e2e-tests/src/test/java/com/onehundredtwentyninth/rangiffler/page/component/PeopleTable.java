package com.onehundredtwentyninth.rangiffler.page.component;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.onehundredtwentyninth.rangiffler.condition.PeopleCollectionCondition.containsUser;
import static com.onehundredtwentyninth.rangiffler.condition.PeopleCollectionCondition.peopleExactly;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;

public class PeopleTable extends BaseComponent<PeopleTable> {

  public PeopleTable(SelenideElement self) {
    super(self);
  }

  public ElementsCollection getAllRows() {
    return $$x("//tbody/tr");
  }

  public SelenideElement getRowByUsername(String username) {
    var allRows = getAllRows();
    var table = $x("//table");
    table.shouldBe(Condition.visible);
    return allRows.find(text(username));
  }

  public PeopleTable exactlyUsersShouldBePresentedInTable(TestUser... users) {
    getAllRows().shouldHave(peopleExactly(users));
    return this;
  }

  public PeopleTable usersShouldBePresentedInTable(TestUser... users) {
    for (var expectedUser : users) {
      getAllRows().should(containsUser(expectedUser));
    }
    return this;
  }

  public PeopleTable usersCountShouldBeEqualTo(int expectedCount) {
    getAllRows().shouldHave(size(expectedCount));
    return this;
  }

  public PeopleTable addFriend(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Add']").click();
    return this;
  }

  public PeopleTable acceptInvitation(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Accept']").click();
    return this;
  }

  public PeopleTable rejectInvitation(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Decline']").click();
    return this;
  }

  public PeopleTable deleteFriend(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Remove']").click();
    return this;
  }
}
