package com.onehundredtwentyninth.rangiffler.client;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryByCodeRequest;
import com.onehundredtwentyninth.rangiffler.grpc.GetCountryRequest;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.model.CountryJson;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GeoClient {

  @GrpcClient("grpcGeoClient")
  private RangifflerGeoServiceBlockingStub rangifflerGeoServiceBlockingStub;

  public List<CountryJson> getAllCountries() {
    var response = rangifflerGeoServiceBlockingStub.getAllCountries(Empty.getDefaultInstance());

    return response.getAllCountriesList()
        .stream()
        .map(CountryJson::fromGrpcMessage)
        .toList();
  }

  public CountryJson getCountry(UUID countryId) {
    var request = GetCountryRequest.newBuilder().setId(countryId.toString()).build();
    var response = rangifflerGeoServiceBlockingStub.getCountry(request);
    return CountryJson.fromGrpcMessage(response);
  }

  public CountryJson getCountryByCode(String code) {
    var response = rangifflerGeoServiceBlockingStub.getCountryByCode(
        GetCountryByCodeRequest.newBuilder().setCode(code).build()
    );
    return CountryJson.fromGrpcMessage(response);
  }
}