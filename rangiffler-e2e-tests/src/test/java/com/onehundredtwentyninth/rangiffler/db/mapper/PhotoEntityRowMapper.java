package com.onehundredtwentyninth.rangiffler.db.mapper;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;


public class PhotoEntityRowMapper implements RowMapper<PhotoEntity> {

  @Override
  public PhotoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    var photoEntity = new PhotoEntity();
    photoEntity.setId(rs.getObject("id", UUID.class));
    photoEntity.setUserId(rs.getObject("user_id", UUID.class));
    photoEntity.setCountryId(rs.getObject("country_id", UUID.class));
    photoEntity.setDescription(rs.getString("description"));
    photoEntity.setPhoto(rs.getBytes("photo"));
    photoEntity.setCreatedDate(rs.getTimestamp("created_date"));
    return photoEntity;
  }
}
