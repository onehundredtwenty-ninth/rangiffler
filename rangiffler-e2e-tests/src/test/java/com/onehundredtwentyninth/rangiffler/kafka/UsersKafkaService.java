package com.onehundredtwentyninth.rangiffler.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehundredtwentyninth.rangiffler.model.api.UserJson;
import lombok.SneakyThrows;

public class UsersKafkaService {

  private final ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  public UserJson getRegisteredUser(String username) {
    return mapper.readValue(
        KafkaReader.getMessageByPredicate("users", s -> s.value().contains(username)).value(),
        UserJson.class
    );
  }

  @SneakyThrows
  public UserJson getRegisteredUser() {
    return mapper.readValue(
        KafkaReader.getLastMessageFromTopic("users").value(),
        UserJson.class
    );
  }
}
