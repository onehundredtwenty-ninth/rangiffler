package com.onehundredtwentyninth.rangiffler.api;

import static io.restassured.RestAssured.given;

import com.onehundredtwentyninth.rangiffler.api.interceptor.RequestCookieInterceptor;
import com.onehundredtwentyninth.rangiffler.api.interceptor.ResponseCodeInterceptor;
import com.onehundredtwentyninth.rangiffler.api.interceptor.ResponseCookieInterceptor;
import com.onehundredtwentyninth.rangiffler.model.TokenResponse;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;

public class AuthClient extends BaseClient {

  public AuthClient() {
    super(CFG.authUrl(),
        true,
        List.of(new RequestCookieInterceptor()),
        List.of(new ResponseCookieInterceptor(), new ResponseCodeInterceptor())
    );
  }

  public Response authorize(String responseType, String clientId, String scope, String redirectUri,
      String codeChallenge, String codeChallengeMethod) {
    return given()
        .spec(requestSpecification)
        .log().all()
        .params(Map.of(
            "response_type", responseType,
            "client_id", clientId,
            "scope", scope,
            "redirect_uri", redirectUri,
            "code_challenge", codeChallenge,
            "code_challenge_method", codeChallengeMethod
        ))
        .get("/oauth2/authorize")
        .then()
        .log().all()
        .statusCode(200)
        .extract()
        .response();
  }

  public Response login(String username, String password, String csrf) {
    return given()
        .spec(requestSpecification)
        .contentType(ContentType.URLENC)
        .log().all()
        .formParams(Map.of(
            "username", username,
            "password", password,
            "_csrf", csrf
        ))
        .post("/login")
        .then()
        .log().all()
        .statusCode(302)
        .extract()
        .response();
  }

  public TokenResponse token(String basicAuthorization, String clientId, String redirectUri, String grantType,
      String code, String codeVerifier) {
    return given()
        .spec(requestSpecification)
        .header("Authorization", basicAuthorization)
        .log().all()
        .formParams(Map.of(
            "client_id", clientId,
            "redirect_uri", redirectUri,
            "grant_type", grantType,
            "code", code,
            "code_verifier", codeVerifier
        ))
        .post("/oauth2/token")
        .then()
        .log().all()
        .statusCode(200)
        .extract()
        .response()
        .as(TokenResponse.class);
  }
}
