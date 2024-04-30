package com.onehundredtwentyninth.rangiffler.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestLike {

  private UUID id;
  private UUID userId;
  private LocalDateTime creationDate;
}
