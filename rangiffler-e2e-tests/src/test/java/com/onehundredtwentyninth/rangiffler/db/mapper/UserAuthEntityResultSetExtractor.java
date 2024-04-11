package com.onehundredtwentyninth.rangiffler.db.mapper;

import com.onehundredtwentyninth.rangiffler.db.model.Authority;
import com.onehundredtwentyninth.rangiffler.db.model.AuthorityEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserAuthEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;


public class UserAuthEntityResultSetExtractor implements ResultSetExtractor<UserAuthEntity> {

  @Override
  public UserAuthEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
    var user = new UserAuthEntity();
    boolean userProcessed = false;

    while (rs.next()) {
      if (!userProcessed) {
        user.setId(rs.getObject(1, UUID.class));
        user.setUsername(rs.getString(2));
        user.setPassword(rs.getString(3));
        user.setEnabled(rs.getBoolean(4));
        user.setAccountNonExpired(rs.getBoolean(5));
        user.setAccountNonLocked(rs.getBoolean(6));
        user.setCredentialsNonExpired(rs.getBoolean(7));
        userProcessed = true;
      }

      var authority = new AuthorityEntity();
      authority.setId(rs.getObject(8, UUID.class));
      authority.setAuthority(Authority.valueOf(rs.getString(10)));
      user.getAuthorities().add(authority);
    }
    return user;
  }
}
