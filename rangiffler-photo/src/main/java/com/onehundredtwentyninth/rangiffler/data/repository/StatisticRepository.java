package com.onehundredtwentyninth.rangiffler.data.repository;

import com.onehundredtwentyninth.rangiffler.data.GroupedStatistic;
import com.onehundredtwentyninth.rangiffler.data.StatisticEntity;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatisticRepository extends JpaRepository<StatisticEntity, UUID> {

  @Query("select new com.onehundredtwentyninth.rangiffler.data.GroupedStatistic(s.countryId, sum(s.count)) "
      + "from StatisticEntity s "
      + "where s.userId in :userIds "
      + "group by s.countryId")
  List<GroupedStatistic> countStatistic(@Nonnull List<UUID> userIds);

  Optional<StatisticEntity> findByUserIdAndCountryId(UUID userId, UUID countryId);
}
