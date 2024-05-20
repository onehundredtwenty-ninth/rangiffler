package com.onehundredtwentyninth.rangiffler.model.testdata;

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
@ToString(exclude = "flag")
public class TestCountry {

  private UUID id;
  private String code;
  private String name;
  private byte[] flag;
}
