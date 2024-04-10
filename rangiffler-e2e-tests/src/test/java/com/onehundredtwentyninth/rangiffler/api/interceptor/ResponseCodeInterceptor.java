package com.onehundredtwentyninth.rangiffler.api.interceptor;

import com.onehundredtwentyninth.rangiffler.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

public class ResponseCodeInterceptor implements HttpResponseInterceptor {

  @Override
  public void process(HttpResponse response, HttpContext context) {
    if (response.getFirstHeader("Location") != null) {
      final String location = response.getFirstHeader("Location").getValue();
      if (location.contains("code=")) {
        final String code = StringUtils.substringAfter(location, "code=");
        AuthService.CodeHolder.setCode(code);
      }
    }
  }
}
