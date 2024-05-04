package com.onehundredtwentyninth.rangiffler.client;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.mapper.CountryMapper;
import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GeoClient {

  @GrpcClient("grpcGeoClient")
  private RangifflerGeoServiceBlockingStub rangifflerGeoServiceBlockingStub;

  public List<GqlCountry> getAllCountries() {
    var response = rangifflerGeoServiceBlockingStub.getAllCountries(Empty.getDefaultInstance());

    return response.getAllCountriesList()
        .stream()
        .map(CountryMapper::fromGrpcMessage)
        .toList();
  }

  public GqlCountry getCountry(UUID countryId) {
    var request = GetCountryRequest.newBuilder().setId(countryId.toString()).build();
    var response = rangifflerGeoServiceBlockingStub.getCountry(request);
    return CountryMapper.fromGrpcMessage(response);
  }

  public GqlCountry getCountryByCode(String code) {
    var response = rangifflerGeoServiceBlockingStub.getCountryByCode(
        GetCountryByCodeRequest.newBuilder().setCode(code).build()
    );
    return CountryMapper.fromGrpcMessage(response);
  }
}
