package com.onehundredtwentyninth.rangiffler.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import lombok.SneakyThrows;

public class OauthUtils {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public static String generateCodeVerifier() {
    var codeVerifier = new byte[32];
    SECURE_RANDOM.nextBytes(codeVerifier);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
  }

  @SneakyThrows
  public static String generateCodeChallange(String codeVerifier) {
    var bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
    var messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(bytes, 0, bytes.length);
    var digest = messageDigest.digest();
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }
}
