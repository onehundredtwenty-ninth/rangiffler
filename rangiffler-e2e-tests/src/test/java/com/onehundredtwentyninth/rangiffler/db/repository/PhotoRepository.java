package com.onehundredtwentyninth.rangiffler.db.repository;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import java.util.UUID;

public interface PhotoRepository {

  PhotoEntity createPhoto(PhotoEntity photo);

  void deletePhoto(UUID id);
}
