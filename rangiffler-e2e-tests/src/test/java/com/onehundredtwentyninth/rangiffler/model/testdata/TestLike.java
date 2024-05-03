package com.onehundredtwentyninth.rangiffler.model.testdata;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestLike {

  private UUID id;
  private UUID userId;
  private LocalDateTime creationDate;
}
