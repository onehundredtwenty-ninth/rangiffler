package com.onehundredtwentyninth.rangiffler.logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * Необходим для логирования запросов / ответов с помощью переданного Logger вместо дефолтной печати в консоль. Logger
 * передаем из класса-клиента, так как при большом количестве тестов это позволит быстрее понять что за запрос / ответ
 * мы перед собой видим
 */
public class RestAssuredLogger {

  private PrintStream restAssuredLogsPrintStream;

  @SneakyThrows
  public PrintStream getPrintStream(Level level, Logger log) {
    if (restAssuredLogsPrintStream == null) {
      OutputStream outputStream = new OutputStream() {
        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        @Override
        public void write(int b) {
          byteArrayOutputStream.write(b);
        }

        @Override
        public void flush() {
          if (!byteArrayOutputStream.toString().isBlank()) {
            var output = String.valueOf(byteArrayOutputStream);
            switch (level) {
              case INFO -> log.info(output);
              case DEBUG -> log.debug(output);
              default -> log.trace(output);
            }
          }
          byteArrayOutputStream.reset();
        }
      };
      restAssuredLogsPrintStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);
    }
    return restAssuredLogsPrintStream;
  }

  @SneakyThrows
  public PrintStream getPrintStream(Logger log) {
    return getPrintStream(Level.INFO, log);
  }
}
