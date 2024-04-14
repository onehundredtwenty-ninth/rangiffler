package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipEntity;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class FriendshipRepositorySJdbc implements FriendshipRepository {

  private final JdbcTemplate udTemplate = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA));

  @Override
  public void createFriendship(UUID firstFriendId, UUID secondFriendId, LocalDateTime createdDate,
      FriendshipStatus friendshipStatus) {
    udTemplate.update("INSERT INTO friendship "
        + "(requester_id, addressee_id, created_date, status) "
        + "VALUES(?, ?, ?, ?)", firstFriendId, secondFriendId, createdDate, friendshipStatus.name()
    );
  }

  @Override
  public Optional<FriendshipEntity> findFriendshipByRequesterIdAndAddresseeId(UUID requesterId, UUID addresseeId) {
    try {
      return Optional.ofNullable(
          udTemplate.queryForObject("SELECT * FROM \"friendship\" WHERE requester_id = ? AND addressee_id = ?",
              (ResultSet rs, int rowNum) -> {
                var friendshipEntity = new FriendshipEntity();
                friendshipEntity.setId(rs.getObject("id", UUID.class));
                friendshipEntity.setRequesterId(rs.getObject("requester_id", UUID.class));
                friendshipEntity.setAddresseeId(rs.getObject("addressee_id", UUID.class));
                friendshipEntity.setCreatedDate(rs.getTimestamp("created_date"));
                friendshipEntity.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                return friendshipEntity;
              }, requesterId, addresseeId)
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
