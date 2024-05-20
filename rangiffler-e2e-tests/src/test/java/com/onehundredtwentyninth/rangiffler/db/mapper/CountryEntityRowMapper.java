package com.onehundredtwentyninth.rangiffler.db.mapper;

import com.onehundredtwentyninth.rangiffler.db.model.CountryEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;


public class CountryEntityRowMapper implements RowMapper<CountryEntity> {

  @Override
  public CountryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    var countryEntity = new CountryEntity();
    countryEntity.setId(rs.getObject("id", UUID.class));
    countryEntity.setCode(rs.getString("code"));
    countryEntity.setName(rs.getString("name"));
    countryEntity.setFlag(rs.getBytes("flag"));
    return countryEntity;
  }
}
