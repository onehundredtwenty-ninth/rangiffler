package com.onehundredtwentyninth.service;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.data.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.grpc.AllCountriesResponse;
import com.onehundredtwentyninth.rangiffler.grpc.Country;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.nio.charset.StandardCharsets;
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
}
