package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.LikeEntity;
import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.model.StatisticEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhotoRepository {

  PhotoEntity createPhoto(PhotoEntity photo);

  void deletePhoto(UUID id);

  PhotoEntity findRequiredPhotoById(UUID photoId);

  Optional<PhotoEntity> findPhotoById(UUID photoId);

  Boolean isPhotoExists(UUID photoId);

  List<PhotoEntity> findByUserId(UUID userId);

  Optional<StatisticEntity> findStatisticByUserIdAndCountryId(UUID userId, UUID countryId);

  void updateStatisticByUserIdAndCountryId(UUID userId, UUID countryId, Integer count);

  void createStatisticByUserIdAndCountryId(UUID userId, UUID countryId, Integer count);

  void deleteStatisticByUserIdAndCountryId(UUID userId, UUID countryId);

  void likePhoto(UUID userId, UUID photoId, LocalDate createdDate);

  List<UUID> findLikesIdsByPhotoId(UUID photoId);

  List<LikeEntity> findLikesByPhotoId(UUID photoId);

  void deleteLikesByIds(List<UUID> likeIds);
}
