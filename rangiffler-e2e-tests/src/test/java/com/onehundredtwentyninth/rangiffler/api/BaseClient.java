package com.onehundredtwentyninth.rangiffler.api;

import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.logger.RestAssuredLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;

public abstract class BaseClient {

  protected static final Config CFG = Config.getInstance();
  protected final RequestSpecification requestSpecification;

  public BaseClient(String baseUri) {
    this.requestSpecification = new RequestSpecBuilder().setBaseUri(baseUri).build();
  }

  @SuppressWarnings("deprecation")
  public BaseClient(String baseUri, boolean followRedirect, List<HttpRequestInterceptor> requestInterceptors,
      List<HttpResponseInterceptor> responseInterceptors) {

    var clientFactory = new HttpClientConfig.HttpClientFactory() {

      @Override
      public HttpClient createHttpClient() {
        var defaultHttpClient = new DefaultHttpClient();

        if (requestInterceptors != null) {
          for (var interceptor : requestInterceptors) {
            defaultHttpClient.addRequestInterceptor(interceptor);
          }
        }

        if (responseInterceptors != null) {
          for (var interceptor : responseInterceptors) {
            defaultHttpClient.addResponseInterceptor(interceptor);
          }
        }

        return defaultHttpClient;
      }
    };

    this.requestSpecification = new RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setConfig(
            RestAssuredConfig.newConfig()
                .redirect(RedirectConfig.redirectConfig().followRedirects(followRedirect).maxRedirects(2))
                .httpClient(HttpClientConfig.httpClientConfig().httpClientFactory(clientFactory))
        )
        .build();
  }

  public List<Filter> filterWithResponseBody(Logger logger) {
    return List.of(
        new RequestLoggingFilter(LogDetail.ALL, new RestAssuredLogger().getPrintStream(logger)),
        new ResponseLoggingFilter(LogDetail.ALL, new RestAssuredLogger().getPrintStream(logger)),
        new AllureRestAssured()
    );
  }

  public List<Filter> filterWithoutResponseBody(Logger logger) {
    return List.of(
        new RequestLoggingFilter(LogDetail.ALL, new RestAssuredLogger().getPrintStream(logger)),
        new ResponseLoggingFilter(LogDetail.STATUS, new RestAssuredLogger().getPrintStream(logger)),
        new AllureRestAssured()
    );
  }
}
