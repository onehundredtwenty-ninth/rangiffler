package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.model.StatisticEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhotoRepository {

  PhotoEntity createPhoto(PhotoEntity photo);

  void deletePhoto(UUID id);

  PhotoEntity findPhotoById(UUID photoId);

  Optional<StatisticEntity> findStatisticByUserIdAndCountryId(UUID userId, UUID countryId);

  void updateStatisticByUserIdAndCountryId(UUID userId, UUID countryId, Integer count);

  void createStatisticByUserIdAndCountryId(UUID userId, UUID countryId, Integer count);

  void deleteStatisticByUserIdAndCountryId(UUID userId, UUID countryId);

  void likePhoto(UUID userId, UUID photoId, LocalDate createdDate);

  List<UUID> findLikesByPhotoId(UUID photoId);

  void deleteLikesByIds(List<UUID> likeIds);
}
