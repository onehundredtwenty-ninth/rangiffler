package com.onehundredtwentyninth.rangiffler.api.interceptor;

import com.onehundredtwentyninth.rangiffler.api.cookie.ThreadSafeCookieManager;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

public class RequestCookieInterceptor implements HttpRequestInterceptor {

  @Override
  public void process(HttpRequest request, HttpContext context) {
    request.addHeader("Cookie", ThreadSafeCookieManager.INSTANCE.cookiesAsString());
  }
}
