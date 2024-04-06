package com.onehundredtwentyninth.service;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.data.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.Country;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GeoService extends RangifflerGeoServiceGrpc.RangifflerGeoServiceImplBase {

  private final CountryRepository countryRepository;

  @Autowired
  public GeoService(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  @Override
  public void getAllCountries(Empty request, StreamObserver<AllCountriesResponse> responseObserver) {
    var allCountriesEntities = countryRepository.findAll();

    var allCountriesResponse = AllCountriesResponse.newBuilder().addAllAllCountries(
            allCountriesEntities.stream().map(
                entity -> Country.newBuilder()
                    .setId(entity.getId().toString())
                    .setCode(entity.getCode())
                    .setName(entity.getName())
                    .setFlag(new String(entity.getFlag(), StandardCharsets.UTF_8))
                    .build()
            ).toList()
        )
        .build();

    responseObserver.onNext(allCountriesResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getCountry(GetCountryRequest request, StreamObserver<Country> responseObserver) {
    var countryEntity = countryRepository.findById(UUID.fromString(request.getId())).orElseThrow();

    var countryResponse = Country.newBuilder()
        .setId(countryEntity.getId().toString())
        .setCode(countryEntity.getCode())
        .setName(countryEntity.getName())
        .setFlag(new String(countryEntity.getFlag(), StandardCharsets.UTF_8))
        .build();

    responseObserver.onNext(countryResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getCountryByCode(GetCountryByCodeRequest request, StreamObserver<Country> responseObserver) {
    var countryEntity = countryRepository.findByCode(request.getCode()).orElseThrow();

    var countryResponse = Country.newBuilder()
        .setId(countryEntity.getId().toString())
        .setCode(countryEntity.getCode())
        .setName(countryEntity.getName())
        .setFlag(new String(countryEntity.getFlag(), StandardCharsets.UTF_8))
        .build();

    responseObserver.onNext(countryResponse);
    responseObserver.onCompleted();
  }
}
