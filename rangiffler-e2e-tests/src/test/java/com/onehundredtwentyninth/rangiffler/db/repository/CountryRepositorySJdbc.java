package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class CountryRepositorySJdbc implements CountryRepository {

  private final JdbcTemplate countryTemplate = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.GEO));

  @Override
  public CountryEntity getCountryByCode(String code) {
    return Optional.ofNullable(
        countryTemplate.queryForObject("SELECT * FROM \"country\" WHERE code = ?", (ResultSet rs, int rowNum) -> {
          var countryEntity = new CountryEntity();
          countryEntity.setId(rs.getObject("id", UUID.class));
          countryEntity.setCode(rs.getString("code"));
          countryEntity.setName(rs.getString("name"));
          countryEntity.setFlag(rs.getBytes("flag"));
          return countryEntity;
        }, code)
    ).orElseThrow();
  }
}
