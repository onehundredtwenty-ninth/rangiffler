package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.mapper.PhotoMapper;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class PhotoDbService implements PhotoTestService {

  private final PhotoRepository photoRepository = new PhotoRepositorySJdbc();
  private final CountryRepository countryRepository = new CountryRepositorySJdbc();

  @Override
  public Photo createPhoto(UUID userId, String countryCode, String image, String description) {
    var country = countryRepository.getCountryByCode(countryCode);

    var photoEntity = new PhotoEntity();
    photoEntity.setUserId(userId);
    photoEntity.setCountryId(country.getId());
    photoEntity.setDescription(description);
    photoEntity.setPhoto(getImageAsBase64(image).getBytes(StandardCharsets.UTF_8));
    photoEntity.setCreatedDate(Timestamp.from(Instant.now()));

    photoEntity = photoRepository.createPhoto(photoEntity);
    return PhotoMapper.toMessage(photoEntity);
  }

  @Override
  public void deletePhoto(UUID id) {
    photoRepository.deletePhoto(id);
  }
}