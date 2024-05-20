package com.onehundredtwentyninth.rangiffler.api.interceptor;

import com.onehundredtwentyninth.rangiffler.api.cookie.ThreadSafeCookieManager;
import java.net.HttpCookie;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

public class ResponseCookieInterceptor implements HttpResponseInterceptor {

  @Override
  public void process(HttpResponse response, HttpContext context) {
    var cookieHeaders = response.getHeaders("Set-cookie");
    for (var cookieHeader : cookieHeaders) {
      var httpCookie = new HttpCookie(
          cookieHeader.getElements()[0].getName(),
          cookieHeader.getElements()[0].getValue()
      );
      var requestUri = context.getAttribute("http.target_host").toString();
      ThreadSafeCookieManager.INSTANCE.add(URI.create(requestUri), httpCookie);
    }
  }
}
