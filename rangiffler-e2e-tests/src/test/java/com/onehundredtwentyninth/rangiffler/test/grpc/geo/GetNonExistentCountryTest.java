package com.onehundredtwentyninth.rangiffler.test.grpc.geo;

import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.GEO)
@Feature(Features.COUNTRY)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.GEO), @Tag(JUnitTags.COUNTRY)})
class GetNonExistentCountryTest extends GrpcGeoTestBase {

  @DisplayName("Получение страны по несуществующему коду")
  @Test
  void getCountryByNonExistentCodeTest() {
    var request = GetCountryByCodeRequest.newBuilder().setCode("nonExistentCode").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getCountryByCode(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: Country nonExistentCode not found");
  }

  @DisplayName("Получение страны по несуществующему id")
  @Test
  void getCountryByNonExistentIdTest() {
    var request = GetCountryRequest.newBuilder().setId("00000000-0000-0000-0000-000000000000").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getCountry(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("NOT_FOUND: Country 00000000-0000-0000-0000-000000000000 not found");
  }

  @DisplayName("Получение страны по невалидному id")
  @Test
  void getCountryByInvalidIdTest() {
    var request = GetCountryRequest.newBuilder().setId("notValidId").build();
    Assertions.assertThatThrownBy(() -> blockingStub.getCountry(request))
        .isInstanceOf(StatusRuntimeException.class)
        .hasMessage("ABORTED: Invalid UUID string: notValidId");
  }
}
