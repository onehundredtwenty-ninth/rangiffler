package com.onehundredtwentyninth.rangiffler.client;

import com.onehundredtwentyninth.rangiffler.grpc.RangifflerStatisticServiceGrpc.RangifflerStatisticServiceBlockingStub;
import com.onehundredtwentyninth.rangiffler.grpc.StatisticRequest;
import com.onehundredtwentyninth.rangiffler.model.GqlCountry;
import com.onehundredtwentyninth.rangiffler.model.GqlStat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticClient {

  @GrpcClient("grpcPhotoClient")
  private RangifflerStatisticServiceBlockingStub rangifflerStatisticServiceBlockingStub;
  @Autowired
  private UsersClient usersClient;

  public List<GqlStat> getStatistic(String userName, boolean withFriends) {
    var userIds = new ArrayList<UUID>();
    userIds.add(usersClient.getUser(userName).id());

    if (withFriends) {
      userIds.addAll(usersClient.getUserFriendsIds(userName));
    }

    var response = rangifflerStatisticServiceBlockingStub.getStatistic(
        StatisticRequest.newBuilder().addAllUserIds(userIds.stream().map(UUID::toString).toList()).build()
    );

    return response.getStatisticList().stream()
        .map(s -> new GqlStat(s.getCount(), new GqlCountry(UUID.fromString(s.getCountryId()), null, null, null)))
        .toList();
  }
}
