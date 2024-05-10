package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.onehundredtwentyninth.rangiffler.kafka.KafkaWriter;

public class KafkaProducerExtension implements SuiteExtension {

  @Override
  public void afterSuite() {
    KafkaWriter.close();
  }
}
