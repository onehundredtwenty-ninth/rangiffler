package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.DataSourceProvider;
import com.onehundredtwentyninth.rangiffler.db.JdbcUrl;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.model.StatisticEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
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
    return photoTxt.execute(status -> {
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

      var statistic = findStatisticByUserIdAndCountryId(photo.getUserId(), photo.getCountryId());
      if (statistic.isPresent()) {
        updateStatisticByUserIdAndCountryId(photo.getUserId(), photo.getCountryId(), statistic.get().getCount() + 1);
      } else {
        createStatisticByUserIdAndCountryId(photo.getUserId(), photo.getCountryId(), 1);
      }

      return photo;
    });
  }

  @Override
  public void deletePhoto(UUID id) {
    photoTxt.execute(status -> {
      var photo = findPhotoById(id);
      var statistic = findStatisticByUserIdAndCountryId(photo.getUserId(), photo.getCountryId()).orElseThrow();

      if (statistic.getCount() - 1 == 0) {
        deleteStatisticByUserIdAndCountryId(photo.getUserId(), photo.getCountryId());
      } else {
        updateStatisticByUserIdAndCountryId(photo.getUserId(), photo.getCountryId(), statistic.getCount() - 1);
      }

      photoTemplate.update("DELETE FROM \"photo_like\" WHERE photo_id = ?", id);
      photoTemplate.update("DELETE FROM \"photo\" WHERE id = ?", id);
      return null;
    });
  }

  @Override
  public PhotoEntity findPhotoById(UUID photoId) {
    return Optional.ofNullable(
        photoTemplate.queryForObject("SELECT * FROM \"photo\" WHERE id = ?",
            (ResultSet rs, int rowNum) -> {
              var photoEntity = new PhotoEntity();
              photoEntity.setId(rs.getObject("id", UUID.class));
              photoEntity.setUserId(rs.getObject("user_id", UUID.class));
              photoEntity.setCountryId(rs.getObject("country_id", UUID.class));
              photoEntity.setDescription(rs.getString("description"));
              photoEntity.setPhoto(rs.getBytes("photo"));
              photoEntity.setCreatedDate(rs.getTimestamp("created_date"));
              return photoEntity;
            },
            photoId
        )
    ).orElseThrow();
  }

  @Override
  public Optional<StatisticEntity> findStatisticByUserIdAndCountryId(UUID userId, UUID countryId) {
    try {
      return Optional.ofNullable(
          photoTemplate.queryForObject("SELECT * FROM \"statistic\" WHERE user_id = ? and country_id = ?",
              (ResultSet rs, int rowNum) -> {
                var statisticEntity = new StatisticEntity();
                statisticEntity.setId(rs.getObject("id", UUID.class));
                statisticEntity.setUserId(rs.getObject("user_id", UUID.class));
                statisticEntity.setCountryId(rs.getObject("country_id", UUID.class));
                statisticEntity.setCount(rs.getInt("count"));
                return statisticEntity;
              },
              userId, countryId
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void updateStatisticByUserIdAndCountryId(UUID userId, UUID countryId, Integer count) {
    photoTemplate.update("UPDATE \"statistic\" SET count = ? WHERE user_id = ? and country_id = ?",
        count, userId, countryId);
  }

  @Override
  public void createStatisticByUserIdAndCountryId(UUID userId, UUID countryId, Integer count) {
    photoTemplate.update("INSERT INTO \"statistic\" (user_id, country_id, count) VALUES(?, ?, ?)",
        userId, countryId, count);
  }

  @Override
  public void deleteStatisticByUserIdAndCountryId(UUID userId, UUID countryId) {
    photoTemplate.update("DELETE FROM \"statistic\" WHERE user_id = ? and country_id = ?", userId, countryId);
  }
}
