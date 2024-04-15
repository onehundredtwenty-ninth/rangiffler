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
import com.onehundredtwentyninth.rangiffler.grpc.StatisticRequest;
import com.onehundredtwentyninth.rangiffler.grpc.StatisticResponse;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.WithPhoto;
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
class GetStatisticTest extends GrpcStatisticTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("Получение статистики")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = "cn", image = "France.png"),
          @WithPhoto(countryCode = "cn", image = "France.png"),
          @WithPhoto(countryCode = "ca", image = "France.png")
      }
  )
  @Test
  void getStatisticTest(User user) {
    final StatisticRequest request = StatisticRequest.newBuilder()
        .addAllUserIds(List.of(user.getId()))
        .build();
    final StatisticResponse response = blockingStub.getStatistic(request);

    final CountryEntity cnCountry = countryRepository.findCountryByCode("cn");
    final CountryEntity caCountry = countryRepository.findCountryByCode("ca");

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(response.getStatisticCount())
          .describedAs("Количество элементов в массиве со статистикой")
          .isEqualTo(2);

      softAssertions.assertThat(response.getStatisticList().get(0).getCountryId())
          .describedAs("ID страны с кодом ca")
          .isEqualTo(caCountry.getId().toString());

      softAssertions.assertThat(response.getStatisticList().get(0).getCount())
          .describedAs("Количество фото в статистике для ca")
          .isEqualTo(1);

      softAssertions.assertThat(response.getStatisticList().get(1).getCountryId())
          .describedAs("ID страны с кодом cn")
          .isEqualTo(cnCountry.getId().toString());

      softAssertions.assertThat(response.getStatisticList().get(1).getCount())
          .describedAs("Количество фото в статистике для cn")
          .isEqualTo(2);
    });
  }
}
