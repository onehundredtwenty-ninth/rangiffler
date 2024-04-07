package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.data.UserEntity;
import com.onehundredtwentyninth.rangiffler.data.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KafkaUserService {

  private static final Logger LOG = LoggerFactory.getLogger(KafkaUserService.class);
  private final UserRepository userRepository;

  @Autowired
  public KafkaUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  @KafkaListener(topics = "users", groupId = "userdata")
  public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
    LOG.info("### Kafka topic [users] received message: {}", user.username());
    LOG.info("### Kafka consumer record: {}", cr);
    var userDataEntity = new UserEntity();
    userDataEntity.setUsername(user.username());
    userDataEntity.setFirstname(user.username());
    userDataEntity.setLastName(user.username());
    userDataEntity.setCountryId("4cca3bae-f195-11ee-9b32-0242ac110002");
    var userEntity = userRepository.save(userDataEntity);
    LOG.info("### User {} successfully saved to database with id: {}", user.username(), userEntity.getId());
  }
}
