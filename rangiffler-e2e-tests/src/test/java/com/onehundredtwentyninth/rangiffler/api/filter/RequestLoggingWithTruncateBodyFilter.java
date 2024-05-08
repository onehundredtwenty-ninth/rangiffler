package com.onehundredtwentyninth.rangiffler.api.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.print.RequestPrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Необходим для сокрытия в логах запросов передаваемых картинок, так как они забивают лог и делает его менее
 * читабельным. Текущая реализация обходит все поля запроса и обрезает строки длиннее 1000 символов
 *
 * @see com.onehundredtwentyninth.rangiffler.grpc.interceptor.GrpcConsoleWithoutByteStringInterceptor
 */
@Slf4j
public class RequestLoggingWithTruncateBodyFilter implements Filter {

  private final ObjectMapper mapper = new ObjectMapper();
  private final Set<LogDetail> logDetail;
  private final PrintStream stream;

  public RequestLoggingWithTruncateBodyFilter(Set<LogDetail> logDetail, PrintStream stream) {
    this.logDetail = logDetail;
    this.stream = stream;
  }

  @Override
  public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
      FilterContext ctx) {
    try {
      RequestPrinter.print(requestSpec, requestSpec.getMethod(), requestSpec.getURI(), logDetail,
          Collections.emptySet(), stream, true);

      Map<String, Object> bodyAsMap = mapper.readValue(requestSpec.getBody().toString(), new TypeReference<>() {
      });
      var bodyToLog = getBodyForLog(bodyAsMap);

      stream.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyToLog));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ctx.next(requestSpec, responseSpec);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getBodyForLog(Map<String, Object> body) {
    for (var field : body.entrySet()) {
      if (field.getValue() == null) {
        body.put(field.getKey(), null);
      } else if (!field.getValue().getClass().isAssignableFrom(LinkedHashMap.class)) {
        var valueToPrint = field.getValue() instanceof String s && s.length() > 1000
            ? s.substring(0, 1000) + "... more"
            : field.getValue();
        body.put(field.getKey(), valueToPrint);
      } else {
        var messages = (Map<String, Object>) field.getValue();
        body.put(field.getKey(), getBodyForLog(messages));
      }
    }
    return body;
  }
}
