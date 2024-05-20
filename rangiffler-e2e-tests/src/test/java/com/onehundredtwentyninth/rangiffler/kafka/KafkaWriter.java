package com.onehundredtwentyninth.rangiffler.kafka;

import com.onehundredtwentyninth.rangiffler.config.Config;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

@Slf4j
public class KafkaWriter {

  private static final Config CFG = Config.getInstance();
  private static final Producer<String, String> stringProducer;

  static {
    var properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CFG.kafkaAddress());
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    stringProducer = new KafkaProducer<>(properties);
  }

  public static void sendRecordToTopic(ProducerRecord<String, String> producerRecord) {
    stringProducer.send(producerRecord, (metadata, exception) -> {
          if (exception != null) {
            log.error(exception.getMessage(), exception);
          } else {
            log.info("Produced event to topic {}: key = {}, value = {}",
                producerRecord.topic(),
                producerRecord.key(),
                producerRecord.value()
            );
          }
        }
    );
  }

  public static void close() {
    stringProducer.close();
  }
}
