package com.onehundredtwentyninth.rangiffler.mapper;

import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import com.onehundredtwentyninth.rangiffler.model.testdata.TestCountry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CountryMapper {

  public static TestCountry toTestCountry(CountryEntity countryEntity) {
    return new TestCountry(
        countryEntity.getId(),
        countryEntity.getCode(),
        countryEntity.getName(),
        countryEntity.getFlag()
    );
  }
}
