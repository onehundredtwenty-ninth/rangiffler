package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import java.util.UUID;

public interface CountryRepository {

  CountryEntity findRequiredCountryById(UUID id);

  CountryEntity findRequiredCountryByCode(String code);

  CountryEntity findRequiredCountryByIdNot(UUID id);

  Integer count();
}
