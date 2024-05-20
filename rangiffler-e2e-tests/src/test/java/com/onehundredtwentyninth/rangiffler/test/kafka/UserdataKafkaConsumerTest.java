package com.onehundredtwentyninth.rangiffler.test.kafka;

import com.github.javafaker.Faker;
import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.assertion.EntitySoftAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.kafka.UsersKafkaService;
import com.onehundredtwentyninth.rangiffler.model.api.UserJson;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.Objects;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.USERS)
@Feature(Features.REGISTER)
@Tags({@Tag(Layers.KAFKA), @Tag(Suites.SMOKE), @Tag(JUnitTags.USERS), @Tag(JUnitTags.REGISTER)})
@DisplayName("[kafka] Userdata")
class UserdataKafkaConsumerTest extends KafkaTestBase {

  @Inject
  private UserRepository userRepository;
  @Inject
  private UsersKafkaService kafkaService;
  private String username;

  @DisplayName("[kafka] Регистрация пользователя в userdata из сообщения в kafka")
  @Test
  void messageShouldBeConsumedFromKafkaAfterSendToTopic() {
    username = new Faker().name().username();
    var user = new UserJson(username, "RU");

    kafkaService.sendUserJsonToTopic(user);

    var dbUser = Awaitility.await()
        .atMost(Duration.ofMillis(5000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> userRepository.findRequiredByUsername(username),
            Objects::nonNull
        );

    EntitySoftAssertions.assertSoftly(softAssertions ->
        softAssertions.assertThat(dbUser)
            .hasUsername(username)
            .firstNameIsNull()
            .lastNameIsNull()
    );
  }

  @AfterEach
  void after() {
    userRepository.deleteInUserdataByUsername(username);
  }
}
