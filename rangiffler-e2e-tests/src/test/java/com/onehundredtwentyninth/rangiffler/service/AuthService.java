package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.api.AuthClient;
import com.onehundredtwentyninth.rangiffler.api.cookie.ThreadSafeCookieManager;
import com.onehundredtwentyninth.rangiffler.config.Config;
import com.onehundredtwentyninth.rangiffler.model.api.TokenResponse;
import com.onehundredtwentyninth.rangiffler.utils.OauthUtils;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthService {

  private static final Config CFG = Config.getInstance();
  private final AuthClient authClient = new AuthClient();

  /**
   * Запросов отправляется несколько больше, чем в Retrofit версии, так как согласно RFC 2616 при редиректе POST запроса
   * со статусом 302 Found автоматический переход производится не должен https://habr.com/ru/articles/86258/
   */
  public TokenResponse doLogin(String username, String password) {
    final String codeVerifier = OauthUtils.generateCodeVerifier();
    final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);

    authClient.authorize("code", "client", "openid", CFG.frontUrl() + "/authorized",
        codeChallenge, "S256");
    authClient.login(username, password, ThreadSafeCookieManager.INSTANCE.getCookieValue("XSRF-TOKEN"));
    authClient.authorize("code", "client", "openid", CFG.frontUrl() + "/authorized",
        codeChallenge, "S256");

    return authClient.token(
        "Basic " + new String(Base64.getEncoder().encode("client:secret".getBytes(StandardCharsets.UTF_8))),
        "client",
        CFG.frontUrl() + "/authorized",
        "authorization_code",
        CodeHolder.getCode(),
        codeVerifier);
  }

  public void doRegister(String username, String password) {
    authClient.registerForm();
    authClient.register(username, password, ThreadSafeCookieManager.INSTANCE.getCookieValue("XSRF-TOKEN"));
  }

  public static class CodeHolder {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static void setCode(String code) {
      HOLDER.set(code);
    }

    public static String getCode() {
      var code = HOLDER.get();
      HOLDER.remove();
      return code;
    }
  }
}
