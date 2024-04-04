package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.GroupedStatistic;
import com.onehundredtwentyninth.data.StatisticEntity;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatisticRepository extends JpaRepository<StatisticEntity, UUID> {

  @Query("select new com.onehundredtwentyninth.data.GroupedStatistic(s.countryId, sum(s.count)) "
      + "from StatisticEntity s "
      + "where s.userId in :userIds "
      + "group by s.countryId")
  List<GroupedStatistic> countStatistic(@Nonnull List<UUID> userIds);
}
