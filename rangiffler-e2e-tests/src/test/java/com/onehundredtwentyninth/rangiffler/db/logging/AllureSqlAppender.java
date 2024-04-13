package com.onehundredtwentyninth.rangiffler.db.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import org.apache.commons.lang3.StringUtils;

public class AllureSqlAppender extends StdoutLogger {

  private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();

  @Override
  public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
    if (StringUtils.isNoneEmpty(sql)) {
      var attachment = new SqlAttachment(
          sql.split("\\s+")[0] + " query to :" + StringUtils.substringBefore(url, "?"),
          SqlFormatter.of(Dialect.PlSql).format(sql)
      );
      attachmentProcessor.addAttachment(attachment, new FreemarkerAttachmentRenderer("sql-query.ftl"));
    }
  }
}
