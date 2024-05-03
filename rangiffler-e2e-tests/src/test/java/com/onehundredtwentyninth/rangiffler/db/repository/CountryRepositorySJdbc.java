package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.mapper.CountryEntityRowMapper;
import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class CountryRepositorySJdbc implements CountryRepository {

  private final JdbcTemplate countryTemplate = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.GEO));

  @Override
  public CountryEntity findRequiredCountryById(UUID id) {
    return countryTemplate.queryForObject("SELECT * FROM \"country\" WHERE id = ?", new CountryEntityRowMapper(), id);
  }

  @Override
  public CountryEntity findRequiredCountryByCode(String code) {
    return countryTemplate.queryForObject("SELECT * FROM \"country\" WHERE code = ?", new CountryEntityRowMapper(),
        code);
  }

  @Override
  public CountryEntity findRequiredCountryByIdNot(UUID id) {
    return countryTemplate.queryForObject("SELECT * FROM \"country\" WHERE id != ? LIMIT 1",
        new CountryEntityRowMapper(),
        id);
  }

  @Override
  public Integer count() {
    return countryTemplate.queryForObject("SELECT COUNT(*) FROM \"country\"", Integer.class);
  }
}
