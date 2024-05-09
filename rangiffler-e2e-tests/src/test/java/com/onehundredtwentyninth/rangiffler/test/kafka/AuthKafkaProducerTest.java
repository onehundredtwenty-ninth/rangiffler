package com.onehundredtwentyninth.rangiffler.test.kafka;

import com.github.javafaker.Faker;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.kafka.UsersKafkaService;
import com.onehundredtwentyninth.rangiffler.service.AuthService;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.Objects;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.REGISTER)
@Tags({@Tag(Layers.KAFKA), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.REGISTER)})
@DisplayName("[kafka] Userdata")
class AuthKafkaProducerTest extends KafkaTestBase {

  @Inject
  private UserService userService;
  @Inject
  private AuthService authService;
  @Inject
  private UserRepository userRepository;
  @Inject
  private UsersKafkaService kafkaService;
  private String username;

  @DisplayName("[kafka] Наличие в kafka сообщения о зарегистрированном пользователе")
  @Test
  void messageShouldBeProducedToKafkaAfterSuccessfulRegistration() {
    username = new Faker().name().username();
    var password = "12345";
    authService.doRegister(username, password);

    var userFromKafka = kafkaService.getRegisteredUser(username);

    Assertions.assertEquals(
        username,
        userFromKafka.username()
    );
  }

  @AfterEach
  void after() {
    Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> userRepository.findRequiredByUsername(username),
            Objects::nonNull
        );
    userService.deleteUser(username);
  }
}
