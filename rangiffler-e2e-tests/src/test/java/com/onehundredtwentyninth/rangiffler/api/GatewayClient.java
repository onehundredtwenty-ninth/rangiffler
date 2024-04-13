package com.onehundredtwentyninth.rangiffler.api;

import static io.restassured.RestAssured.given;

import com.onehundredtwentyninth.rangiffler.model.GqlCountryResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.GqlResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayClient extends BaseClient {

  public GatewayClient() {
    super(CFG.gatewayUrl(), log);
  }

  public GqlResponse<GqlCountryResponse> getCountries(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .auth().oauth2(bearerToken)
        .contentType(ContentType.JSON)
        .when()
        .body(request)
        .post("/graphql")
        .then()
        .statusCode(200)
        .extract()
        .response()
        .as(new TypeRef<>() {
        });
  }
}
