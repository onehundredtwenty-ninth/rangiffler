package com.onehundredtwentyninth.rangiffler.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCountry {

  private UUID id;
  private String code;
  private String name;
  private byte[] flag;
}
