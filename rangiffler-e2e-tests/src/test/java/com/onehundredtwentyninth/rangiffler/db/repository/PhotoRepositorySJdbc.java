package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class PhotoRepositorySJdbc implements PhotoRepository {

  private final JdbcTemplate photoTemplate;
  private final TransactionTemplate photoTxt;

  public PhotoRepositorySJdbc() {
    var photoTm = new JdbcTransactionManager(DataSourceProvider.INSTANCE.dataSource(JdbcUrl.PHOTO));
    this.photoTxt = new TransactionTemplate(photoTm);
    this.photoTemplate = new JdbcTemplate(Objects.requireNonNull(photoTm.getDataSource()));
  }

  @Override
  public PhotoEntity createPhoto(PhotoEntity photo) {
    var kh = new GeneratedKeyHolder();
    photoTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO photo "
              + "(user_id, country_id, description, photo, created_date) "
              + "VALUES(?, ?, ?, ?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS
      );
      ps.setObject(1, photo.getUserId());
      ps.setObject(2, photo.getCountryId());
      ps.setString(3, photo.getDescription());
      ps.setBytes(4, photo.getPhoto());
      ps.setTimestamp(5, photo.getCreatedDate());
      return ps;
    }, kh);

    photo.setId((UUID) Objects.requireNonNull(kh.getKeys()).get("id"));
    return photo;
  }

  @Override
  public void deletePhoto(UUID id) {
    photoTxt.execute(status -> {
      photoTemplate.update("DELETE FROM \"photo_like\" WHERE photo_id = ?", id);
      photoTemplate.update("DELETE FROM \"photo\" WHERE id = ?", id);
      return null;
    });
  }
}
