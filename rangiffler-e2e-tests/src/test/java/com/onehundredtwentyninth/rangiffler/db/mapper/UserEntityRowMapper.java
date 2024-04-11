package com.onehundredtwentyninth.rangiffler.db.mapper;

import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;


public class UserEntityRowMapper implements RowMapper<UserEntity> {

  @Override
  public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEntity user = new UserEntity();
    user.setId(rs.getObject("id", UUID.class));
    user.setUsername(rs.getString("username"));
    user.setFirstname(rs.getString("firstname"));
    user.setLastName(rs.getString("last_name"));
    user.setAvatar(rs.getBytes("avatar"));
    return user;
  }
}
