package com.onehundredtwentyninth.rangiffler.service;

import com.google.protobuf.Empty;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.model.CountryJson;
import jakarta.annotation.Nonnull;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class GeoClient {

  @GrpcClient("grpcGeoClient")
  private RangifflerGeoServiceBlockingStub rangifflerGeoServiceBlockingStub;

  public @Nonnull List<CountryJson> getAllCountries() {
    var response = rangifflerGeoServiceBlockingStub.getAllCountries(Empty.getDefaultInstance());

    return response.getAllCountriesList()
        .stream()
        .map(CountryJson::fromGrpcMessage)
        .toList();
  }
}
