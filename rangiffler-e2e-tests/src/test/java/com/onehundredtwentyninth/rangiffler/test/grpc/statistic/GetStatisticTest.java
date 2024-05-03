package com.onehundredtwentyninth.rangiffler.test.grpc.statistic;

import com.google.inject.Inject;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.grpc.CountryStatisticResponse;
import com.onehundredtwentyninth.rangiffler.grpc.StatisticRequest;
import com.onehundredtwentyninth.rangiffler.grpc.StatisticResponse;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.STATISTIC)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.STATISTIC)})
@DisplayName("[grpc] Statistic")
class GetStatisticTest extends GrpcStatisticTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("[grpc] Получение статистики")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.FRANCE)
      }
  )
  @Test
  void getStatisticTest(TestUser user) {
    final StatisticRequest request = StatisticRequest.newBuilder()
        .addAllUserIds(List.of(user.getId().toString()))
        .build();
    final StatisticResponse response = blockingStub.getStatistic(request);

    final CountryEntity cnCountry = countryRepository.findRequiredCountryByCode("cn");
    final CountryEntity caCountry = countryRepository.findRequiredCountryByCode("ca");

    final CountryStatisticResponse expectedStatisticForCn = CountryStatisticResponse.newBuilder()
        .setCountryId(cnCountry.getId().toString())
        .setCount(2)
        .build();
    final CountryStatisticResponse expectedStatisticForCa = CountryStatisticResponse.newBuilder()
        .setCountryId(caCountry.getId().toString())
        .setCount(1)
        .build();

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response.getStatisticCount())
          .describedAs("Количество элементов в массиве со статистикой")
          .isEqualTo(2);

      softAssertions.assertThat(response.getStatisticList())
          .describedAs("Статистика страны с кодом cn")
          .contains(expectedStatisticForCn);

      softAssertions.assertThat(response.getStatisticList())
          .describedAs("Статистика страны с кодом ca")
          .contains(expectedStatisticForCa);
    });
  }
}
