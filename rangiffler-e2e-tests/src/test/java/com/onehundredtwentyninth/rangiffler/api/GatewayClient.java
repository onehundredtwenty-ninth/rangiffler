package com.onehundredtwentyninth.rangiffler.api;

import static io.restassured.RestAssured.given;

import com.onehundredtwentyninth.rangiffler.model.GqlCountryResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlDeletePhotoResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlFeedResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlPeopleResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlPhotoResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import com.onehundredtwentyninth.rangiffler.model.GqlResponse;
import com.onehundredtwentyninth.rangiffler.model.GqlUserResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayClient extends BaseClient {

  public GatewayClient() {
    super(CFG.gatewayUrl());
  }

  public GqlResponse<GqlCountryResponse> getCountries(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithoutResponseBody(log))
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

  public GqlResponse<GqlUserResponse> getUser(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithResponseBody(log))
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

  public GqlResponse<GqlPeopleResponse> getPeople(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithoutResponseBody(log))
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

  public GqlResponse<GqlFeedResponse> getFeed(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithoutResponseBody(log))
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

  public GqlResponse<GqlUserResponse> updateUser(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithoutResponseBody(log))
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

  public GqlResponse<GqlPhotoResponse> updatePhoto(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithoutResponseBody(log))
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

  public GqlResponse<GqlDeletePhotoResponse> deletePhoto(String bearerToken, GqlRequest request) {
    return given()
        .spec(requestSpecification)
        .filters(filterWithResponseBody(log))
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
