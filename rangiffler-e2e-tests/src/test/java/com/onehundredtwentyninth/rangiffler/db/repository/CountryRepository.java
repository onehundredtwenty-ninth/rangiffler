package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import java.util.UUID;

public interface CountryRepository {

  CountryEntity findCountryById(UUID id);

  CountryEntity findCountryByCode(String code);

  CountryEntity findCountryByIdNot(UUID id);

  Integer count();
}
