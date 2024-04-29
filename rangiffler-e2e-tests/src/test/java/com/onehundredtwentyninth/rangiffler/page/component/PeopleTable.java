package com.onehundredtwentyninth.rangiffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.onehundredtwentyninth.rangiffler.condition.PeopleCollectionCondition.people;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.onehundredtwentyninth.rangiffler.model.TestUser;

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

  public PeopleTable usersShouldBePresentedInTable(TestUser... users) {
    getAllRows().shouldHave(people(users));
    return this;
  }
}
