package com.onehundredtwentyninth.rangiffler.kafka;

import com.onehundredtwentyninth.rangiffler.config.Config;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;

@Slf4j
public class KafkaReader implements Runnable {

  private static final Config CFG = Config.getInstance();
  private static final Properties STR_KAFKA_PROPERTIES = new Properties();
  private static final Map<String, List<ConsumerRecord<String, String>>> STORE = new ConcurrentHashMap<>();
  private final AtomicBoolean threadStarted = new AtomicBoolean(true);
  private final Consumer<String, String> stringConsumer;

  static {
    STR_KAFKA_PROPERTIES.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CFG.kafkaAddress());
    STR_KAFKA_PROPERTIES.put(ConsumerConfig.GROUP_ID_CONFIG, "stringKafkaStringConsumerService");
    STR_KAFKA_PROPERTIES.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    STR_KAFKA_PROPERTIES.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    STR_KAFKA_PROPERTIES.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  }

  public KafkaReader() {
    this.stringConsumer = new KafkaConsumer<>(STR_KAFKA_PROPERTIES);
    this.stringConsumer.subscribe(CFG.kafkaTopics());
    CFG.kafkaTopics().forEach(s -> STORE.put(s, Collections.synchronizedList(new LinkedList<>())));
  }

  public void stop() {
    this.threadStarted.set(false);
  }

  public static ConsumerRecord<String, String> getMessageByPredicate(String topic,
      Predicate<ConsumerRecord<String, String>> predicate) {
    return Awaitility.await()
        .atMost(Duration.ofMillis(15000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .pollInSameThread()
        .until(
            () -> STORE.get(topic).stream().filter(predicate).findFirst().orElse(null),
            Objects::nonNull
        );
  }

  public static ConsumerRecord<String, String> getLastMessageFromTopic(String topic) {
    return Awaitility.await()
        .atMost(Duration.ofMillis(15000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> STORE.get(topic).get(STORE.get(topic).size() - 1),
            Objects::nonNull
        );
  }

  @Override
  public void run() {
    try {
      while (threadStarted.get()) {
        var records = stringConsumer.poll(Duration.ofMillis(500));
        for (var record : records) {
          logRecord(record);
          STORE.get(record.topic()).add(record);
        }
        try {
          stringConsumer.commitSync();
        } catch (CommitFailedException e) {
          log.error(e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      stringConsumer.close();
      Thread.currentThread().interrupt();
    }
  }

  private void logRecord(@Nonnull ConsumerRecord<String, String> record) {
    log.info("topic = {}, headers = {}, \npartition = {}, \noffset = {}, \nkey = {}, \nvalue = {}\n\n",
        record.topic(),
        record.headers(),
        record.partition(),
        record.offset(),
        record.key(),
        record.value());
  }
}
