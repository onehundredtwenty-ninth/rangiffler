package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;

public interface CountryRepository {

  CountryEntity findCountryByCode(String code);

  Integer count();
}
