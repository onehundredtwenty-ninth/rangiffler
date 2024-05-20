package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.onehundredtwentyninth.rangiffler.kafka.KafkaReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.extension.ExtensionContext;

public class KafkaExtension implements SuiteExtension {

  private final KafkaReader kafkaReader = new KafkaReader();
  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  @Override
  public void beforeSuite(ExtensionContext extensionContext) {
    executor.execute(kafkaReader);
    executor.shutdown();
  }

  @Override
  public void afterSuite() {
    kafkaReader.stop();
  }
}
