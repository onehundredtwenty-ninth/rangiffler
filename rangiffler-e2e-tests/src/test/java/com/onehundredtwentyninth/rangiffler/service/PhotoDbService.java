package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.mapper.PhotoMapper;
import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class PhotoDbService implements PhotoService {

  private final PhotoRepository photoRepository = new PhotoRepositorySJdbc();
  private final CountryRepository countryRepository = new CountryRepositorySJdbc();

  @Override
  public TestPhoto createPhoto(UUID userId, String countryCode, String image, String description) {
    var country = countryRepository.findCountryByCode(countryCode);

    var photoEntity = new PhotoEntity();
    photoEntity.setUserId(userId);
    photoEntity.setCountryId(country.getId());
    photoEntity.setDescription(description);
    photoEntity.setPhoto(getImageAsBase64(image).getBytes(StandardCharsets.UTF_8));
    photoEntity.setCreatedDate(Timestamp.from(Instant.now()));

    photoEntity = photoRepository.createPhoto(photoEntity);
    return PhotoMapper.toTestPhoto(photoEntity);
  }

  @Override
  public void deletePhoto(UUID id) {
    photoRepository.deletePhoto(id);
  }

  @Override
  public void likePhoto(UUID userId, UUID photoId) {
    photoRepository.likePhoto(userId, photoId, LocalDate.now());
  }
}
