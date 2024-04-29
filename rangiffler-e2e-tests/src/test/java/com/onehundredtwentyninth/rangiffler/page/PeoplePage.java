package com.onehundredtwentyninth.rangiffler.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.onehundredtwentyninth.rangiffler.page.component.PeopleTable;

public class PeoplePage extends BasePage<PeoplePage> {

  private final SelenideElement friendsTab = $x("//button[text()='Friends']");
  private final PeopleTable table = new PeopleTable($("//table"));

  public PeoplePage open() {
    Selenide.open("/people");
    return this;
  }

  public PeoplePage openFriendsTab() {
    friendsTab.click();
    return this;
  }

  public PeoplePage userShouldBePresentedInTable(String username) {
    table.getRowByUsername(username).shouldBe(visible);
    return this;
  }
}
