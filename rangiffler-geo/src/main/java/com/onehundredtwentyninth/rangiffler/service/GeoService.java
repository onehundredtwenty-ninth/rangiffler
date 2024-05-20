package com.onehundredtwentyninth.rangiffler.service;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.data.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.exception.CountryNotFoundException;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.Country;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import com.onehundredtwentyninth.rangiffler.mapper.CountryMapper;
import io.grpc.stub.StreamObserver;
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
            allCountriesEntities.stream().map(CountryMapper::toMessage).toList()
        )
        .build();

    responseObserver.onNext(allCountriesResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getCountry(GetCountryRequest request, StreamObserver<Country> responseObserver) {
    var countryEntity = countryRepository.findById(UUID.fromString(request.getId()))
        .orElseThrow(() -> new CountryNotFoundException(request.getId()));

    var countryResponse = CountryMapper.toMessage(countryEntity);
    responseObserver.onNext(countryResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void getCountryByCode(GetCountryByCodeRequest request, StreamObserver<Country> responseObserver) {
    var countryEntity = countryRepository.findByCode(request.getCode())
        .orElseThrow(() -> new CountryNotFoundException(request.getCode()));

    var countryResponse = CountryMapper.toMessage(countryEntity);
    responseObserver.onNext(countryResponse);
    responseObserver.onCompleted();
  }
}
