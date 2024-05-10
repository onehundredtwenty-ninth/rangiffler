package com.onehundredtwentyninth.rangiffler.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehundredtwentyninth.rangiffler.model.api.UserJson;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;

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

  @SneakyThrows
  public void sendUserJsonToTopic(UserJson user) {
    var userJson = mapper.writeValueAsString(user);
    String key = null;
    var typeHeader = new RecordHeader(
        "__TypeId__",
        "com.onehundredtwentyninth.rangiffler.model.UserJson".getBytes(StandardCharsets.UTF_8)
    );
    KafkaWriter.sendRecordToTopic(new ProducerRecord<>("users", null, key, userJson, List.of(typeHeader)));
  }
}
