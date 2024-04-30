package com.onehundredtwentyninth.rangiffler.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestPhoto {

  private UUID id;
  private UUID userId;
  private TestCountry country;
  private String description;
  private byte[] photo;
  private LocalDateTime createdDate;
  private List<TestLike> likes;
}
