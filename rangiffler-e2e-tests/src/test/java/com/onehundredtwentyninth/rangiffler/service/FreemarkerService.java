package com.onehundredtwentyninth.rangiffler.service;

import com.github.javafaker.Faker;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

public class FreemarkerService {

  public Configuration getDefaultConfiguration() {
    var cfg = new Configuration(Configuration.VERSION_2_3_30);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setNumberFormat("#");
    cfg.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
    return cfg;
  }

  public Map<String, Object> getCommonTemplateData() {
    return new HashMap<>(
        Map.of(
            "enFaker", new Faker(),
            "ruFaker", new Faker(new Locale("ru")),
            "days", TimeUnit.DAYS,
            "hours", TimeUnit.HOURS,
            "minutes", TimeUnit.MINUTES
        )
    );
  }

  @SneakyThrows
  public String replaceTokens(String templateName, String str) {
    var cfg = getDefaultConfiguration();
    var strTemp = new StringTemplateLoader();
    strTemp.putTemplate(templateName, str);
    cfg.setTemplateLoader(strTemp);

    var template = cfg.getTemplate(templateName);
    var templateData = getCommonTemplateData();

    try (var writer = new StringWriter()) {
      template.process(templateData, writer);
      return writer.toString();
    }
  }

  public String replaceTokens(String str) {
    var templateName = UUID.randomUUID().toString();
    return replaceTokens(templateName, str);
  }
}
