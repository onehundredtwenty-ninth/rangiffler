package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.mapper.UserEntityRowMapper;
import com.onehundredtwentyninth.rangiffler.db.model.UserAuthEntity;
import com.onehundredtwentyninth.rangiffler.db.model.UserEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

public class UserRepositorySJdbc implements UserRepository {

  private final TransactionTemplate authTxt;
  private final TransactionTemplate udTxt;
  private final JdbcTemplate authTemplate;
  private final JdbcTemplate udTemplate;
  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserRepositorySJdbc() {
    var authTm = new JdbcTransactionManager(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH));
    var udTm = new JdbcTransactionManager(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA));

    this.authTxt = new TransactionTemplate(authTm);
    this.udTxt = new TransactionTemplate(udTm);
    this.authTemplate = new JdbcTemplate(Objects.requireNonNull(authTm.getDataSource()));
    this.udTemplate = new JdbcTemplate(Objects.requireNonNull(udTm.getDataSource()));
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    return authTxt.execute(status -> {
      authTemplate.update(con -> {
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO \"user\" " +
                "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, user.getUsername());
        ps.setString(2, pe.encode(user.getPassword()));
        ps.setBoolean(3, user.getEnabled());
        ps.setBoolean(4, user.getAccountNonExpired());
        ps.setBoolean(5, user.getAccountNonLocked());
        ps.setBoolean(6, user.getCredentialsNonExpired());
        return ps;
      }, kh);

      user.setId((UUID) Objects.requireNonNull(kh.getKeys()).get("id"));

      authTemplate.batchUpdate("INSERT INTO \"authority\" " +
          "(user_id, authority) " +
          "VALUES (?, ?)", new BatchPreparedStatementSetter() {
        @Override
        public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
        }

        @Override
        public int getBatchSize() {
          return user.getAuthorities().size();
        }
      });

      return user;
    });
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    KeyHolder kh = new GeneratedKeyHolder();
    udTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO \"user\" (username, firstname, last_name, avatar, country_id) VALUES (?, ?, ?, ?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getFirstname());
      ps.setString(3, user.getLastName());
      ps.setBytes(4, user.getAvatar());
      ps.setString(5, user.getCountryId().toString());
      return ps;
    }, kh);

    user.setId((UUID) Objects.requireNonNull(kh.getKeys()).get("id"));
    return user;
  }

  @Override
  public void deleteInAuthById(UUID id) {
    authTxt.execute(status -> {
      authTemplate.update("DELETE FROM \"authority\" WHERE user_id = ?", id);
      authTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public void deleteInAuthByUsername(String username) {
    var userId = authTemplate.queryForObject("SELECT id FROM \"user\" WHERE username = ?", UUID.class, username);
    deleteInAuthById(userId);
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    udTxt.execute(status -> {
      udTemplate.update("DELETE FROM friendship WHERE addressee_id = ?", id);
      udTemplate.update("DELETE FROM friendship WHERE requester_id = ?", id);
      udTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public void deleteInUserdataByUsername(String username) {
    var user = findByUsername(username);
    deleteInUserdataById(user.getId());
  }

  @Override
  public Integer count() {
    return udTemplate.queryForObject("SELECT COUNT(*) FROM \"user\"", Integer.class);
  }

  @Override
  public UserEntity findById(UUID id) {
    return udTemplate.queryForObject("SELECT * FROM \"user\" WHERE id = ?", new UserEntityRowMapper(), id);
  }

  @Override
  public UserEntity findByUsername(String username) {
    return udTemplate.queryForObject("SELECT * FROM \"user\" WHERE username = ?", new UserEntityRowMapper(), username);
  }

  @Override
  public UserEntity findByFirstname(String firstname) {
    return udTemplate.queryForObject("SELECT * FROM \"user\" WHERE firstname = ?", new UserEntityRowMapper(),
        firstname);
  }

  @Override
  public UserEntity findByLastname(String lastName) {
    return udTemplate.queryForObject("SELECT * FROM \"user\" WHERE last_name = ?", new UserEntityRowMapper(), lastName);
  }
}
