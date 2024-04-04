package com.onehundredtwentyninth.service;

import com.onehundredtwentyninth.data.repository.StatisticRepository;
import com.onehundredtwentyninth.rangiffler.grpc.CountryStatisticResponse;
import com.onehundredtwentyninth.rangiffler.grpc.RangifflerStatisticServiceGrpc;
import com.onehundredtwentyninth.rangiffler.grpc.StatisticRequest;
import com.onehundredtwentyninth.rangiffler.grpc.StatisticResponse;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class StatisticService extends RangifflerStatisticServiceGrpc.RangifflerStatisticServiceImplBase {

  private final StatisticRepository statisticRepository;

  @Autowired
  public StatisticService(StatisticRepository statisticRepository) {
    this.statisticRepository = statisticRepository;
  }

  @Override
  public void getStatistic(StatisticRequest request, StreamObserver<StatisticResponse> responseObserver) {
    var statistic = statisticRepository.countStatistic(
        request.getUserIdsList().stream().map(UUID::fromString).toList()
    );

    var statisticResponse = StatisticResponse.newBuilder()
        .addAllStatistic(
            statistic.stream().map(entity -> CountryStatisticResponse.newBuilder()
                .setCount(entity.getCount().intValue())
                .setCountryId(entity.getCountryId().toString())
                .build()
            ).toList()
        )
        .build();

    responseObserver.onNext(statisticResponse);
    responseObserver.onCompleted();
  }
}
