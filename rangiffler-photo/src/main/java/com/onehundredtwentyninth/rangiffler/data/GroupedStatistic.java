package com.onehundredtwentyninth.rangiffler.data;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupedStatistic {

  private UUID countryId;
  private Long count;
}
