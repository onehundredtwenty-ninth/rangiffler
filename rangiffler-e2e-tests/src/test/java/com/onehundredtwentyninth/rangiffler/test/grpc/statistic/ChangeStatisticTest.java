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
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.CreateUser;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.WithPhoto;
import com.onehundredtwentyninth.rangiffler.model.testdata.CountryCodes;
import com.onehundredtwentyninth.rangiffler.model.testdata.PhotoFiles;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.STATISTIC)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.STATISTIC)})
@DisplayName("[grpc] Statistic")
class ChangeStatisticTest extends GrpcStatisticTestBase {

  @Inject
  private PhotoRepository photoRepository;
  @Inject
  private CountryRepository countryRepository;

  @DisplayName("[grpc] Изменение статистики при обновлении фото")
  @CreateUser(
      photos = {
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CN, image = PhotoFiles.FRANCE),
          @WithPhoto(countryCode = CountryCodes.CA, image = PhotoFiles.FRANCE)
      }
  )
  @Test
  void getStatisticTest(TestUser user) {
    final CountryEntity cnCountry = countryRepository.findRequiredCountryByCode("cn");
    final CountryEntity caCountry = countryRepository.findRequiredCountryByCode("ca");
    final CountryEntity ruCountry = countryRepository.findRequiredCountryByCode("ru");

    final var updatedPhoto = user.getPhotos()
        .stream().filter(s -> s.getCountry().getId().equals(caCountry.getId()))
        .findFirst()
        .orElseThrow();

    final UpdatePhotoRequest updatePhotoRequest = UpdatePhotoRequest.newBuilder()
        .setUserId(user.getId().toString())
        .setId(updatedPhoto.getId().toString())
        .setCountryId(ruCountry.getId().toString())
        .setDescription(UUID.randomUUID().toString())
        .build();
    photoServiceBlockingStub.updatePhoto(updatePhotoRequest);

    Awaitility.await("Ожидаем обновления фото в БД")
        .atMost(Duration.ofMillis(10000))
        .pollInterval(Duration.ofMillis(1000))
        .ignoreExceptions()
        .until(
            () -> photoRepository.findRequiredPhotoById(updatedPhoto.getId()).getDescription()
                .equals(updatePhotoRequest.getDescription())
        );

    final StatisticRequest request = StatisticRequest.newBuilder()
        .addAllUserIds(List.of(user.getId().toString()))
        .build();
    final StatisticResponse response = blockingStub.getStatistic(request);

    final CountryStatisticResponse expectedStatisticForCn = CountryStatisticResponse.newBuilder()
        .setCountryId(cnCountry.getId().toString())
        .setCount(2)
        .build();
    final CountryStatisticResponse oldStatisticForCa = CountryStatisticResponse.newBuilder()
        .setCountryId(caCountry.getId().toString())
        .setCount(1)
        .build();
    final CountryStatisticResponse expectedStatisticForRu = CountryStatisticResponse.newBuilder()
        .setCountryId(ruCountry.getId().toString())
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
          .describedAs("Статистика страны с кодом ru")
          .contains(expectedStatisticForRu);

      softAssertions.assertThat(response.getStatisticList())
          .describedAs("Статистика страны с кодом ca")
          .doesNotContain(oldStatisticForCa);
    });
  }
}
