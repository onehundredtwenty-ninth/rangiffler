package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.model.TestPhoto;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

public interface PhotoService {

  TestPhoto createPhoto(UUID userId, String countryCode, String image, String description);

  void deletePhoto(UUID id);

  void likePhoto(UUID userId, UUID photoId);

  @SneakyThrows
  default String getImageAsBase64(String imageName) {
    var res = getClass().getClassLoader().getResource("image/" + imageName);
    var imageFile = Paths.get(Objects.requireNonNull(res).toURI()).toFile();
    var fileContent = FileUtils.readFileToByteArray(imageFile);
    return "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
  }
}
