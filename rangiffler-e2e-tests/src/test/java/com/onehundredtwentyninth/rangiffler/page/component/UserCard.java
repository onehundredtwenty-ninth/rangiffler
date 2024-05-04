package com.onehundredtwentyninth.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestCountry;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import org.openqa.selenium.By;

public class UserCard extends BaseComponent<UserCard> {

  private final ElementsCollection attributeCells = self.$$x(".//td");
  private final SelenideElement avatar = self.$x(".//th//img");

  public UserCard(SelenideElement self) {
    super(self);
  }

  public byte[] getAvatar() {
    return Objects.requireNonNull(avatar.getAttribute("src")).getBytes(StandardCharsets.UTF_8);
  }

  public String getUsername() {
    return attributeCells.get(0).getText();
  }

  public String getFirstname() {
    return attributeCells.get(1).getText();
  }

  public String getLastName() {
    return attributeCells.get(2).getText();
  }

  public TestCountry getCountry() {
    return new TestCountry(
        null,
        null,
        attributeCells.get(3).getText(),
        attributeCells.get(3).findElement(By.xpath("img")).getAttribute("src").getBytes(StandardCharsets.UTF_8)
    );
  }

  public boolean equalTo(TestUser expectedUser) {
    return getUsername().equals(expectedUser.getUsername())
        && getFirstname().equals(expectedUser.getFirstname())
        && getLastName().equals(expectedUser.getLastName())
        && getCountry().getName().equals(expectedUser.getCountry().getName())
        && Arrays.equals(getCountry().getFlag(), expectedUser.getCountry().getFlag())
        && Arrays.equals(getAvatar(), expectedUser.getAvatar());
  }

  public TestUser toTestUser() {
    return TestUser.builder()
        .username(getUsername())
        .firstname(getFirstname())
        .lastName(getLastName())
        .country(getCountry())
        .avatar(getAvatar())
        .build();
  }
}
