package com.onehundredtwentyninth.rangiffler.test.unit;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUserExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CreateUserExtension.class)
class CreateUserTest {

  @CreateUser(username = "bee48", password = "123")
  @Test
  void createUserTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertEquals("bee48", user.getUsername()),
        () -> Assertions.assertEquals("bee48", user.getFirstname()),
        () -> Assertions.assertEquals("bee48", user.getLastName()),
        () -> Assertions.assertEquals("4cca3bae-f195-11ee-9b32-0242ac110002", user.getCountryId())
    );
  }

  @CreateUser
  @Test
  void createRandomUserTest(User user) {
    Assertions.assertAll(
        () -> Assertions.assertNotNull(user.getId()),
        () -> Assertions.assertNotNull(user.getUsername()),
        () -> Assertions.assertNotNull(user.getFirstname()),
        () -> Assertions.assertNotNull(user.getLastName()),
        () -> Assertions.assertEquals("4cca3bae-f195-11ee-9b32-0242ac110002", user.getCountryId())
    );
  }
}
