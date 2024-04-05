package com.onehundredtwentyninth.config;

import com.onehundredtwentyninth.rangiffler.model.UserJson;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class RangifflerUserdataConsumerConfiguration {

  private final KafkaProperties kafkaProperties;

  @Autowired
  public RangifflerUserdataConsumerConfiguration(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public ConsumerFactory<String, UserJson> consumerFactory() {
    var props = kafkaProperties.buildConsumerProperties();
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, UserJson> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, UserJson> concurrentKafkaListenerContainerFactory
        = new ConcurrentKafkaListenerContainerFactory<>();
    concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
    return concurrentKafkaListenerContainerFactory;
  }
}
