package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.model.FriendshipStatus;
import java.time.LocalDateTime;
import java.util.UUID;
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
}
